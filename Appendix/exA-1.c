// C 언어의 fork() 프로시저

#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <neinet/in.h>
#include <unistd.h>
#include <stdio.h>

int main(int args, char *argv[]) {
  signal(SIGCHLD, SIG_IGN);
  struct sockaddr_in serv_addr;
  bzero((char *) &serv_addr, sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_addr.s_addr = INADDR_ANY;
  serv_addr.sin_port = htons(8080);
  int server_socket = socket(AF_INET, SOCK_STREAM, 0);
  if (server_socket < 0) {
    perror("socket");
    exit(1);
  }
  listen(server_socket, 100);
  struct sockaddr_in cli_addr;
  socklen_t clilen = sizeof(cli_addr);
  while (1) {
    int client_socket = accept(
      server_socket, (structsockaddr *) &cli_addr, &clilen);
    if (client_socket < 0) {
      perror("accept");
      exit(1);
    }
    int pid = fork();
    if (pid == 0) {
      close(server_socket);
      char buffer[1024];
      while (1) {
	if (read(client_socket,buffer,255) < 0) {
	  perror("read");
	  exit(1);
	}
	if (write(client_socket,
	  "HTTP/1.1 200 OK\r\nContent-length: 2\r\n\r\nOK",
	  40) < 0) {
	  perror("write");
	  exit(1);
	}
      }
    } else {
      if (pid < 0) {
	perror("fork");
	exit(1);
      }
    }
    close(client_socket);
  }
  return 0;
}
