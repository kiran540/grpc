package client;

import com.demo.grpcGreeting.GreeterGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.demo.grpcGreeting.Greeting.*;

public class GreetingClient {

    private static final Logger logger = LoggerFactory.getLogger(GreetingClient.class.getName());
    private static GreeterGrpc.GreeterStub nonBlockingStub;
    private static ManagedChannel channel;

    public static void main(String[] args) {
        StreamObserver<ClientInput> clientInput ;
        channel = ManagedChannelBuilder.forAddress("localhost",9099)
                .usePlaintext()
                .build();
        nonBlockingStub = GreeterGrpc.newStub(channel);
        clientInput = nonBlockingStub.greet(new StreamObserver<ServerOutput>() {
            @Override
            public void onNext(ServerOutput serverOutput) {
                logger.info(serverOutput.getMessage()+" got from server in client at"+new Date());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("Error while sending book stream: " + throwable);
            }

            @Override
            public void onCompleted() {
                logger.info("client received data from server"+new Date());
            }
        });
        for(int i=0;i<5;i++){
            clientInput.onNext(ClientInput.newBuilder()
                    .setName("kiran"+i)
                    .build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            clientInput.onCompleted();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
        private static GreeterGrpc.GreeterStub nonBlockingStub;
    Client-streaming GRPC
    public static void main(String[] args) {
        StreamObserver<ClientInput> clientInput ;
        channel = ManagedChannelBuilder.forAddress("localhost",9099)
                .usePlaintext()
                .build();
        nonBlockingStub = GreeterGrpc.newStub(channel);
        clientInput = nonBlockingStub.greet(new StreamObserver<ServerOutput>() {
            @Override
            public void onNext(ServerOutput serverOutput) {
                logger.info(serverOutput.getMessage()+" got from server in client at"+new Date());
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("Error while sending book stream: " + throwable);
            }

            @Override
            public void onCompleted() {
                logger.info("client received data from server"+new Date());
            }
        });
        for(int i=0;i<5;i++){
            clientInput.onNext(ClientInput.newBuilder()
                    .setName("kiran"+i)
                    .build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            clientInput.onCompleted();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     */
    /*
        private static GreeterGrpc.GreeterBlockingStub blockingStub;
    Server-streaming GRPC
    public static void main(String[] args) {
        channel = ManagedChannelBuilder.forAddress("localhost",9099)
                .usePlaintext()
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
        ClientInput input = ClientInput.newBuilder()
                .setName("kiran")
                .build();
        Iterator<ServerOutput> output = blockingStub.greet(input);
        System.out.println("*********************Response from server*********************\n --> : ");
        while(output.hasNext()){
            System.out.println(output.next().getMessage());
        }
        channel.shutdownNow();
    }
     */
}
