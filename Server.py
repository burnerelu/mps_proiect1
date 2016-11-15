#!/usr/bin/python2.7

import socket
import select
import time
import random
import sys

class Player:
    """
    Each object represents a socket connection
    """
    # Client states
    # 0 = Not ready
    # 1 = Ready
    
    def __init__(self, myid, connection, ip, port, fileno, color):
        """
        Player init
        """
        ### Server side info
        self.id = myid
        self.connection = connection
        self.ip = ip
        self.port = port
        self.fileno = fileno
        
        ### Client side info
        self.status = 0;  
        self.color = color;
	self.name = '.'
        self.state = 0;
        self.gameinfo = ''


    def lobby_info(self):
        """
        Compose a message of player's info
        """
        return self.name + ':' + self.color + ':' + str(self.status)

    def send_message(self, msg):
        """
        Send message through a socket
        """
        try:
            bs = self.connection.send(msg)
            return 0
        except socket.error:
            return -1

class PacmanServer:
    """
    Main server class
    """

    # Server states
    # 0 = lobby
    # 1 = game started

    soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    epoll = select.epoll()
    player_count = 0;
    players = []
    colors = ['Red', 'Green', 'Yellow', 'Blue', 'Purple']
    state = 0
    game_map = None
 
    
    def __init__(self, address="localhost", port=24999):
        """
        Server init
        """
        self.soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.soc.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
        self.soc.bind((address, port))
        self.soc.listen(5)
        self.soc.setblocking(0)
        
        self.epoll.register(self.soc.fileno(), select.EPOLLIN)

    def lobby_info(self, caller):
        """
        Send package of lobby info to a player
        """
        result = caller.lobby_info()
        for player in self.players:
            if player is not caller:
                result = result + ';' + player.lobby_info()

        return result

    def game_info(self, caller):
        """
        Compose message of all players' game info
        """
        result = caller.gameinfo
        for player in self.players:
            if player is not caller:
                result = result + ';' + player.gameinfo
        return result

    def get_player(self, fileno):
        """
        Fetch a player by socket file number
        """
        for player in self.players:
            if player.fileno == fileno:
                return player

        return None

    def clean_players(self):
        """
        Cleans up dead connections from list
        """
        changed = 1
        while(changed == 1):
            changed = 0
            for player in self.players:
                if player.id == -1:
                    changed = 1
                    self.players.remove(player)
                    break


    def get_id(self):
        """
        Get a valid ID for the next connection
        """
        sid = 0;
        status = 0;
        while status == 0:
            status = 1;
            for player in self.players:
                if player.id == sid:
                    status = 0;
                    sid = sid + 1;
                    break;
        return sid;

    def get_color(self):
        """
        Assigns a random color
        """
        color = random.choice(self.colors)
        self.colors.remove(color)
        return color


    def real_name(self, name, index):
        """
        Returns name + (index) if index is not 0
        """
        if index == 0:
            return name
        else:
            return name + '(' + str(index) + ')'


    # Sent message types
    # 
    # 103 - Lobby info
    # 110 - All ready
    # 120 - Game map
    # 301 - Game info

    def send_msg(self, player, message, msgtype):
        """
        Sends a message to a user
        """
        message = str(msgtype) + '<' + str(message) + '>\n'
        rc = player.connection.send(message.encode('utf-8'))

    def broadcast_lobby(self):
        """
        Broadcasts lobby info
        """
        for player in self.players:
            try:
                rc = self.send_msg(player, self.lobby_info(player), 103)
            except socket.error, msg:
		errorcode = msg[0]
                if errorcode == 11:
                    pass
                else:
                    print 'Error - ' + str(msg)
                print player.name + " disconnected"
                player.id = -1;
                self.player_count = self.player_count - 1;
                self.colors.append(player.color)
                self.epoll.unregister(player.connection.fileno())
        self.clean_players()

    def broadcast_game(self):
        """
        Broadcast game info
        """

        for player in self.players:
            try:
                rc = self.send_msg(player, self.game_info(player), 301)
            except socket.error, msg:
                errorcode = msg[0]
                if errorcode == 11:
                    pass
                else:
                    print player.name + " disconnected"
                    sys.exit(1)


    def broadcast(self, message, code):
        """
        Broadcast a message
        """
        for player in self.players:
            try:
                rc = self.send_msg(player, message, code)
            except socket.error:
                print 'Player disconnected after ready check - shutting down'
                sys.exit(1)

        
    def ready_check(self):
        """
        Checks if all players are ready
        """
        for player in self.players:
            if player.status == 0:
                return False

        return True


    def recv_msg(self, player):
        """
        Receives a message from a player
        """
        message = ''
        
        soc = player.connection
        try:
            while True:
                c = soc.recv(1)
                if c == '\n' or c == '':
                    break
                else:
                    message += c
        except socket.error as e:
            print e
          #  print 'Package lost'

        return message

    # Message types
    # 200 - name
    # 201 - ready check
    # 202 - game map 

    def parse_msg(self, player, message):
        """
        Parse message and do stuff with it
        """
	try:
            pieces = message.split('<')

            msgtype = pieces[0]
            msg = pieces[1].split('>')[0]

            if msgtype == "200":
                print 'Player sent name ' + msg
                new_name = self.assign_name(msg)
                print 'Assigned ' + new_name
                player.name = new_name
            elif msgtype == "201":
                if msg == 'ready':
                    player.status = 1
                    print 'Player ' + player.name + ' ready'
                else:
                    player.status = 0
                    print 'Player ' + player.name + ' not ready'
            elif msgtype == "202":
                print 'Map received from ' + player.name
                self.game_map = msg
            elif msgtype == "401":
                player.gameinfo = msg

            message = ''
        except:
            pass


    def assign_name(self, name, index=0):
        """
        Checks if chosen name is already taken
        """

        duplicate = 0
        for player in self.players:
            if player.name == self.real_name(name, index):
                index = index + 1
                duplicate = 1
                break;

        if duplicate == 0:
           return self.real_name(name, index) 
        else:
           return self.assign_name(name, index)
        
            

    def run(self):
        """
        Main loop of the server
        """
        try:
            while True:
                events = self.epoll.poll(1)
                if self.state == 0:
                    # Lobby state

                    for fileno, event in events:
                        if fileno == self.soc.fileno():
                            if self.player_count < 5:
                                conn, (ip, port) = self.soc.accept()
                                conn.setblocking(0)
                                self.epoll.register(conn.fileno(), select.EPOLLIN | select.EPOLLOUT)
                                print "Player from " + str(ip) + ":" + str(port)
                                new_player = Player(self.get_id(), conn, ip, port, conn.fileno(), self.get_color())
                                self.players.append(new_player)
                                self.player_count = self.player_count + 1
                            else:
                                print "Already maximum number of players"

                        elif event & select.EPOLLIN:
                            player = self.get_player(fileno)
                            
                            if player != None:    
                                msg = self.recv_msg(player)
                                if msg != '':
                                    self.parse_msg(player, msg)
                            else:
                                print "Runtime error"
                                sys.exit(1)
                        elif event & select.EPOLLOUT:
                            pass
                        elif event & select.EPOLLHUP:
                            pass
                    
                    self.broadcast_lobby()

                    if self.player_count > 4 and self.ready_check():
                        print "Ready check succeeded\nWarning: disconnect will end in server shutdown\nLoading.."
                        self.broadcast('ready', 110)
                        # TODO: TIME.START here and count 10 seconds until map is received
                        self.state = 1
                        # Clear statuses so ready check can be performed after map load
                        for player in self.players:
                            player.status = 0;
                
                elif self.state == 1:
                    # Pre game
                    for fileno, event in events:
                        if event & select.EPOLLIN:
                            player = self.get_player(fileno)
                            
                            if player != None:    
                                msg = self.recv_msg(player)
                                if msg != '':
                                    self.parse_msg(player, msg)
    
                    if self.game_map is not None:
                        self.broadcast(self.game_map, 120)

                    if self.ready_check():
                        print 'The game has now started'
                        self.state = 2


                elif self.state == 2:
                    # Game
                    for fileno, event in events:
                        if event & select.EPOLLIN:
                            player = self.get_player(fileno)
                                
                            if player != None:    
                                msg = self.recv_msg(player)
                                if msg != '':
                                    self.parse_msg(player, msg)
                    self.broadcast_game() 



                time.sleep (25.0 / 1000.0);
                #TODO After-connection part

        except KeyboardInterrupt:
            print "\nInterrupted by user"

        finally:
            self.epoll.unregister(self.soc.fileno())
            self.epoll.close()
            self.soc.close()


if __name__ == "__main__":
    server = PacmanServer()
    server.run()
