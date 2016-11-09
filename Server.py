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
	self.name = ''
        self.state = 0; 

    def test(self):
        """
        Tests if the connections are still up
        """

        rc = self.send_message('555<SYN>')
        return rc

    def info(self):
        """
        Compose a message of player's info
        """
        return self.name + ':' + self.color + ':' + str(self.status)


    def send_message(self, msg):
        """
        Send message through a socket
        """
        try:
            self.connection.send(msg)
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
 
    
    def __init__(self, address="localhost", port=24999):
        """
        Server init
        """
        self.soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.soc.bind((address, port))
        self.soc.listen(5)
        self.soc.setblocking(0)
        self.epoll.register(self.soc.fileno(), select.EPOLLIN)

    def lobby_info(self, caller):
        """
        Send package of lobby info to a player
        """
        result = caller.info()
        for player in self.players:
            if player is not caller:
                result = result + ';' + player.info()

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

    def check_players(self):
        """
        Checks if connections from list are still up and marks them dead if not
        """
        for player in self.players:
            rc = player.test()
            if(rc < 0):
                player.id = -1
                self.player_count = self.player_count - 1;
		self.colors.append(player.color)
        self.clean_players()

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
    # 100 - SYN (are you still alive?)
    # 101 - Lobby info 
    # 

    def send_msg(self, player, message, msgtype):
        """
        Sends a message to a user
        """
        message = str(msgtype) + '<' + message + '>'
        player.connection.send(message.encode('utf-8'))


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
                if self.state == 0:
                    
                    # Lobby state
                    events = self.epoll.poll(1)

                    for fileno, event in events:
                        if fileno == self.soc.fileno():
                            if self.player_count < 5:
                                conn, (ip, port) = self.soc.accept()
                                conn.setblocking(0)
                                self.epoll.register(conn.fileno(), select.EPOLLIN)
                                print "Player from " + str(ip) + ":" + str(port)
                                new_player = Player(self.get_id(), conn, ip, port, conn.fileno(), self.get_color())
                                self.players.append(new_player)
                                self.player_count = self.player_count + 1
                            else:
                                print "Already maximum number of players"

                        elif event & select.EPOLLIN:
                            player = self.get_player(fileno)
                            if player != None:
                                msg = self.get_player(fileno).connection.recv(1024)
                            else:
                                print "Runtime error"
                                sys.exit(1)
                            self.epoll.modify(fileno, select.EPOLLOUT)
                        elif event & select.EPOLLOUT:
                            pass
                        elif event & select.EPOLLHUP:
                            self.epoll.unregister(fileno)
                            
                            self.get_player(fileno).conn.close()
                            self.get_player(fileno).id = -1
    
                    #self.send_msg(new_player, self.lobby_info(new_player), 101)
                    #new_player.name = self.assign_name(name.rstrip('\n'))
                    #self.players.append(new_player)

                    #self.send_msg(new_player, self.lobby_info(new_player), 101)
    
                    #self.player_count = self.player_count + 1;
                    
                    
                    #if(self.player_count > 4):
                    self.check_players()
                    if(self.player_count > 5):
                        print "Bingo!"
                        self.state = 1
                        break
                else:
                    pass
                #TODO After-connection part
        finally:
            self.epoll.unregister(self.soc.fileno())
            self.epoll.close()
            self.soc.close()


if __name__ == "__main__":
    server = PacmanServer()
    server.run()
