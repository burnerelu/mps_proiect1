#!/usr/bin/python2.7

import socket
import time
import random

class Connection:
    """
    Each object represents a socket connection
    """
    # Client states
    # 0 = Not ready
    # 1 = Ready
    
    def __init__(self, myid, connection, ip, port, color):
        """
        Connection init
        """
        ### Server side info
        self.id = myid
        self.connection = connection
        self.ip = ip
        self.port = port
        
        ### Client side info
        self.status = 0;  
        self.color = color;
	self.name = ''
        self.state = 0; 

    def test(self):
        """
        Tests if the connections are still up
        """

        self.send_message('dummy')
        rc = self.send_message('dummy')
        if rc < 0:
            print "Connection " + str(self.id) + " failed"
            return rc
        else:
            print "Connection " + str(self.id) + " still up"
            return rc

    def info(self):
        """
        Compose a message of connection's info
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
    conn_count = 0;
    connections = []
    colors = ['Red', 'Green', 'Yellow', 'Blue', 'Purple']
    state = 0
 
    
    def __init__(self, address="localhost", port=24999):
        """
        Server init
        """
        self.soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.soc.bind((address, port))
        self.soc.listen(5)

    def lobby_info(self, connection):
        """
        Send package of lobby info to a player
        """
        result = connection.info()
        for conn in self.connections:
            if conn is not connection:
                result = result + ';' + conn.info()

        return result

    def clean_connections(self):
        """
        Cleans up dead connections from list
        """
        changed = 1
        while(changed == 1):
            changed = 0
            for conn in self.connections:
                if conn.id == -1:
                    changed = 1
                    self.connections.remove(conn)
                    break

    def check_connections(self):
        """
        Checks if connections from list are still up and marks them dead if not
        """
        for conn in self.connections:
            rc = conn.test()
            if(rc < 0):
                conn.id = -1
                self.conn_count = self.conn_count - 1;
		self.colors.append(conn.color)
        self.clean_connections()

    def get_id(self):
        """
        Get a valid ID for the next connection
        """
        sid = 0;
        status = 0;
        while(status == 0):
            status = 1;
            for conn in self.connections:
                if conn.id == sid:
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

    def send_msg(self, conn, message, msgtype):
        """
        Sends a message to a user
        """
        message = str(msgtype) + '<' + message + '>'
        conn.connection.send(message.encode('utf-8'))


    def assign_name(self, name, index=0):
        """
        Checks if chosen name is already taken
        """

        duplicate = 0
        for conn in self.connections:
            if conn.name == self.real_name(name, index):
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
        while True:
            if self.state == 0:
                conn, (ip, port) = self.soc.accept()
                print "Connection from " + str(ip) + ":" + str(port)
                new_connection = Connection(self.get_id(), conn, ip, port, self.get_color())

                name = conn.recv(1024)
		new_connection.name = self.assign_name(name.rstrip('\n'))
                self.connections.append(new_connection)
                
                self.send_msg(new_connection, self.lobby_info(new_connection), 101)

                self.conn_count = self.conn_count + 1;
                if(self.conn_count > 4):
                    self.check_connections()
                if(self.conn_count > 4):
                    print "Bingo!"
                    self.state = 1
                    break
            else:
                pass
            #TODO After-connection part


if __name__ == "__main__":
    server = PacmanServer()
    server.run()
