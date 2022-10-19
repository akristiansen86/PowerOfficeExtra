#!/usr/bin/env python3
from io import SEEK_CUR
import socketserver
import json
import os.path
from typing import MutableSet


class MyTCPHandler(socketserver.StreamRequestHandler):
    """
    This class is responsible for handling a request. The whole class is
    handed over as a parameter to the server instance so that it is capable
    of processing request. The server will use the handle-method to do this.
    It is instantiated once for each request!
    Since it inherits from the StreamRequestHandler class, it has two very
    usefull attributes you can use:

    rfile - This is the whole content of the request, displayed as a python
    file-like object. This means we can do readline(), readlines() on it!

    wfile - This is a file-like object which represents the response. We can
    write to it with write(). When we do wfile.close(), the response is
    automatically sent.

    The class has three important methods:
    handle() - is called to handle each request.
    setup() - Does nothing by default, but can be used to do any initial
    tasks before handling a request. Is automatically called before handle().
    finish() - Does nothing by default, but is called after handle() to do any
    necessary clean up after a request is handled.
    """
    
    def handle(self):
        """
        This method is responsible for handling an http-request. 
        """
        def write_json(data, filename = "messages"):
            with open(filename, "w") as f:
                json.dump(data, f, indent = 4)
                f.close()

        self.method, self.path, version = self.rfile.readline().decode('UTF-8').split(' ')
        self.headers, self.data = self.rfile.peek().decode('UTF-8').split('\r\n\r\n') 
        self.rfile.close()
        
        if self.path[0] == "/":
            mypath = self.path[1:]
        else:
            mypath = self.path

        if version == "HTTP/1.1\r\n":
            if self.method == "GET":
                if mypath == "":
                    mypath = "index.html"
                white_list = ["index.html", "test.txt", "messages"]
                if os.path.exists(mypath) ==True:
                    if (mypath in white_list):
                        respons_code = b"HTTP/1.1 200 OK \r\n"
                        
                        if mypath == "":
                            index_file = open("index.html", "rb").read()
                            cont_type = "text/html"
                            cont_len = len(index_file)
                            #print("Ok 200")
                            
                        elif mypath == "/":
                            index_file = open("index.html", "rb").read()
                            cont_type = "text/html"
                            cont_len = len(index_file)
                            #print("Ok 200")
                        
                        elif mypath == "index.html":
                            index_file = open("index.html", "rb").read()
                            cont_type = "text/html"
                            cont_len = len(index_file)
                            #print("Ok 200")
                        
                        elif mypath == "test.txt":
                            test_file = open("test.txt", "a+")
                            test_file.close()

                            index_file = open("test.txt", "rb").read()
                            cont_type = "text/plain"
                            cont_len = len(index_file)
                            #print("Ok 200")

                        else:
                            index_file = open("messages", "rb").read()
                            cont_type = "application/json"
                            cont_len = len(index_file)
                            #print("Ok 200")

                    else:
                        respons_code = b"HTTP/1.1 403 Forbidden \r\n"
                        index_file = open("403.png", "rb").read()
                        cont_type = "image/png"
                        cont_len = len(index_file)
                        #print("Forbidden ERR 403")
                    
                else:
                    respons_code = b"HTTP/1.1 404 Not Found \r\n"
                    index_file = open("404.png", "rb").read()
                    cont_type = "image/png"
                    cont_len = len(index_file)
                    #print("Not Found ERR 404")
                          
            elif self.method == "POST":
                white_list = ["test.txt", "messages"]
                if mypath == "test.txt":
                    if os.path.exists(mypath) != True:
                        with open(mypath, "w") as f:
                            f.close()
                
                if os.path.exists(mypath) ==True:
                    if (mypath in white_list):
                        respons_code = b"HTTP/1.1 200 OK \r\n"
                        if mypath == "test.txt":
                            with open("test.txt", "a+") as test_file:
                                test_file.seek(0)
                                data = test_file.read(100)
                                if len(data) > 0:
                                    test_file.write("\n")
                                test_file.write(self.data)
                            test_file.close()

                            index_file = open("test.txt", "rb").read()
                            cont_type = "text/plain"
                            cont_len = len(index_file)
                            #print("Ok 200")

                        else:
                            with open("messages", "r") as json_file:
                                temp = json.load(json_file)
                                count = len(temp)
                                try :
                                    temp_data = json.loads(self.data)
                                    y = {"id": count+1, "text": temp_data["body"]}
                                    temp.append(y)
                                except:
                                    y = {"id": count+1, "text": self.data}
                                    temp.append(y)
                        
                                write_json(temp)
                                json_file.close()
                            
                            respons_code = b"HTTP/1.1 201 Created \r\n"
                            index_file = open("messages", "rb").read()
                            cont_type = "application/json"
                            cont_len = len(index_file)
                            #print("Ok 200")

                    else:
                        respons_code = b"HTTP/1.1 403 Forbidden \r\n"
                        index_file = open("403.png", "rb").read()
                        cont_type = "image/png"
                        cont_len = len(index_file)
                        #print("Forbidden ERR 403")
                    
                else:
                    respons_code = b"HTTP/1.1 404 Not Found \r\n"
                    index_file = open("404.png", "rb").read()
                    cont_type = "image/png"
                    cont_len = len(index_file)
                    #print("Not Found ERR 404")
            
            elif self.method == "PUT":
                white_list = ["messages"]
                path_parts = mypath.split("/")
                mypath = path_parts[0]
                id_num = int(path_parts[1])
                new_temp = []
                if id_num >= 0: 
                    if os.path.exists(mypath) ==True:
                        if (mypath in white_list):
                            respons_code = b"HTTP/1.1 200 OK \r\n"
                            with open("messages", "r") as json_file:
                                temp = json.load(json_file)
                                for element in temp:
                                    if element["id"] == id_num:
                                        try :
                                            temp_data = json.loads(self.data)
                                            y = {"id": id_num, "text": temp_data["body"]}
                                            new_temp.append(y)
                                        except:
                                            y = {"id": id_num, "text": self.data}
                                            new_temp.append(y)
                                    else:
                                        new_temp.append(element)

                                    #element["text"] = self.data
                                
                                write_json(new_temp)
                                json_file.close()

                            index_file = open("messages", "rb").read()
                            cont_type = "application/json"
                            cont_len = len(index_file)
                            #print("Ok 200")

                        else:
                            respons_code = b"HTTP/1.1 403 Forbidden \r\n"
                            index_file = open("403.png", "rb").read()
                            cont_type = "image/png"
                            cont_len = len(index_file)
                            #print("Forbidden ERR 403")
                        
                    else:
                        respons_code = b"HTTP/1.1 404 Not Found \r\n"
                        index_file = open("404.png", "rb").read()
                        cont_type = "image/png"
                        cont_len = len(index_file)
                        #print("Not Found ERR 404")
                
                else:
                    respons_code = b"HTTP/1.1 404 Not Found \r\n"
                    index_file = open("404.png", "rb").read()
                    cont_type = "image/png"
                    cont_len = len(index_file)
                        

            elif self.method == "DELETE":
                white_list = ["messages"]
                path_parts = mypath.split("/")
                mypath = path_parts[0]
                id_num = int(path_parts[1])
                new_temp = []
                
                if os.path.exists(mypath) ==True:
                    if (mypath in white_list):
                        respons_code = b"HTTP/1.1 200 OK \r\n"
                        with open("messages", "r") as json_file:
                            temp = json.load(json_file)
                            if len(temp) > 0:
                                for element in temp:
                                    if element["id"] != id_num:
                                        new_temp.append(element)                                     
                            
                                #Forny id
                                for i in range(len(new_temp)):
                                    new_temp[i]["id"] = i+1
                            
                            write_json(new_temp)
                            json_file.close()

                        index_file = open("messages", "rb").read()
                        cont_type = "application/json"
                        cont_len = len(index_file)
                        #print("Ok 200")

                    else:
                        respons_code = b"HTTP/1.1 403 Forbidden \r\n"
                        index_file = open("403.png", "rb").read()
                        cont_type = "image/png"
                        cont_len = len(index_file)
                        #print("Forbidden ERR 403")
                    
                else:
                    respons_code = b"HTTP/1.1 404 Not Found \r\n"
                    index_file = open("404.png", "rb").read()
                    cont_type = "image/png"
                    cont_len = len(index_file)
                    #print("Not Found ERR 404")
                                            
            else:
                respons_code = b"HTTP/1.1 405 Method Not Allowed \r\n"
                index_file = open("405.png", "rb").read()
                cont_type = "image/png"
                cont_len = len(index_file)
                #print("Method not allowed ERR 405")

        else:
            respons_code = b"HTTP/1.1 505 HTTP Version Not Supported \r\n"
            index_file = open("505.png", "rb").read()
            cont_type = "image/png"
            cont_len = len(index_file)
            #print("HTTP Version Not Supported ERR 505")
            
        headers = (f"Content-Type: {cont_type} \r\nContent-Length: {cont_len} \r\n\r\n")
        self.wfile.write(respons_code)
        self.wfile.write(headers.encode("UTF-8"))
        self.wfile.write(index_file)
        self.wfile.close()    
        return
    
    


if __name__ == "__main__":
    HOST, PORT = "localhost", 8080
    socketserver.TCPServer.allow_reuse_address = True
    with socketserver.TCPServer((HOST, PORT), MyTCPHandler) as server:
        print("Serving at: http://{}:{}".format(HOST, PORT))
        server.serve_forever()

