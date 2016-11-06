#!/usr/bin/python2.7

import socket
import time


class Connection:
    """
    Each object represents a socket connection
    """
    # Client states
    # 0 = Not ready
    # 1 = Ready
    
    def __init__(self, myid, connection, ip, port):
        """
        Connection init
        """
        self.id = myid
        self.connection = connection
        self.ip = ip
        self.port = port
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

    def send_message(self, msg):
        """
        Send message through a socket
        """
        try:
            sent_size = self.connection.send(msg)
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
    state = 0
 
    

    def __init__(self, address="localhost", port=24999):
        """
        Server init
        """
        self.soc.bind((address, port))
        self.soc.listen(5)

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
            


    def run(self):
        """
        Main loop of the server
        """
        while True:
            if self.state == 0:
                conn, (ip, port) = self.soc.accept()
                print "Got connection from " + str(ip) + ":" + str(port)

                new_connection = Connection(self.get_id(), conn, ip, port)
                self.connections.append(new_connection)

                msg = conn.recv(1024)
                print msg
                conn.send(b'Connection accepted')

                self.conn_count = self.conn_count + 1;
                if(self.conn_count > 2):
                    self.check_connections()
                if(self.conn_count > 2):
                    print "Bingo!"
                    break
            else
                pass
            #TODO After-connection part


if __name__ == "__main__":
    server = PacmanServer()
    server.run()
