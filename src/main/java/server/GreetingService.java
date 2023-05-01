package server;

import static com.demo.grpcGreeting.Greeting.*;

import com.demo.grpcGreeting.GreeterGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class GreetingService extends GreeterGrpc.GreeterImplBase {
    private static final Logger logger = LoggerFactory.getLogger(GreetingService.class.getName());
    private static StringBuilder name = new StringBuilder();

    @Override
    public StreamObserver<ClientInput> greet(StreamObserver<ServerOutput> responseObserver) {
        return new StreamObserver<ClientInput>() {

            @Override
            public void onNext(ClientInput clientInput) {
                logger.info(clientInput.getName()+"\n listening for more..."+new Date());
                name.append(clientInput.getName()+" ");
                responseObserver.onNext(ServerOutput.newBuilder()
                        .setMessage(name.toString()).build());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("Error while reading book stream: " + throwable);
            }

            @Override
            public void onCompleted() {
                logger.info("completed reading from client. Now snding back response"+new Date());
                responseObserver.onCompleted();
            }
        };
    }
/*
    Client-streaming GRPC
    @Override
    public StreamObserver<ClientInput> greet(StreamObserver<ServerOutput> responseObserver) {
        return new StreamObserver<ClientInput>() {

            @Override
            public void onNext(ClientInput clientInput) {
                logger.info(clientInput.getName()+"\n listening for more..."+new Date());
                name.append(clientInput.getName()+" ");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("Error while reading book stream: " + throwable);
            }

            @Override
            public void onCompleted() {
                logger.info("completed reading from client. Now snding back response"+new Date());
                responseObserver.onNext(ServerOutput.newBuilder()
                        .setMessage(name.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }
     */

    /*
    Server-streaming GRPC

    @Override
    public void greet(ClientInput request, StreamObserver<ServerOutput> responseObserver) {
        logger.info("Got Request from client : ");
        logger.info(request.getName());
        ServerOutput output = ServerOutput.newBuilder()
                .setMessage(String.format("Hello %s !.Welcome to grpc World. From Server. ",request.getName()))
                .build();
        for(int i=0;i<5;i++){
            responseObserver.onNext(ServerOutput.newBuilder()
                    .setMessage(String.format("Hello %s ! -- > %d",request.getName(),i))
                    .build());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
     */
}
