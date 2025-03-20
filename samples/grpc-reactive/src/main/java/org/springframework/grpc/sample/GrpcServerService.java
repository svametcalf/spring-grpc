package org.springframework.grpc.sample;

import java.time.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.grpc.sample.proto.HelloReply;
import org.springframework.grpc.sample.proto.HelloRequest;
import org.springframework.grpc.sample.proto.ReactorSimpleGrpc;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GrpcServerService extends ReactorSimpleGrpc.SimpleImplBase {

	private static Log log = LogFactory.getLog(GrpcServerService.class);

	@Override
	public Mono<HelloReply> sayHello(Mono<HelloRequest> request) {
		return request.flatMap(req -> {
			log.info("Hello " + req.getName());
			if (req.getName().startsWith("error")) {
				//return Mono.error(new IllegalArgumentException("Bad name: " + req.getName()));
				throw new IllegalArgumentException("Bad name: " + req.getName());
			}
			if (req.getName().startsWith("internal")) {
				return Mono.error(new RuntimeException());
			}
			return Mono.just(HelloReply.newBuilder().setMessage("Hello ==> " + req.getName()).build());
		});
	}

	@Override
	public Flux<HelloReply> streamHello(Mono<HelloRequest> request) {
		return request.flatMapMany(req -> {
			log.info("Hello " + req.getName());
			return Flux.interval(Duration.ofSeconds(1))
				.take(5)
				.map(i -> HelloReply.newBuilder().setMessage("Hello(" + i + ") ==> " + req.getName()).build());
		});
	}

}