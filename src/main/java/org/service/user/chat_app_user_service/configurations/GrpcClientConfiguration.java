package org.service.user.chat_app_user_service.configurations;

import java.io.File;

import javax.net.ssl.SSLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import proto.IdGeneratorGrpc;

@Slf4j
@Configuration
public class GrpcClientConfiguration {

	private final int idGeneratorServerPort;

	private final String idGeneratorServerAddress;

	private final String caCert;

	private final String clientCert;

	private final String clientKey;

	private final boolean usingTlS;

	public GrpcClientConfiguration() {
		this.idGeneratorServerPort = Integer.parseInt(System.getenv("ID_SERVICE_PORT"));
		this.idGeneratorServerAddress = System.getenv("ID_SERVICE_ADDRESS");
		this.usingTlS = System.getenv("ID_TLS").equals("true");

		String certDir = System.getenv("CERT_DIR");
		this.caCert = certDir + "/ca_cert.pem";
		this.clientCert = certDir + "/client_cert.pem";
		this.clientKey = certDir + "/client_key.pem";
	}

	@Bean
	public ManagedChannel managedChannel() throws SSLException {
		File caCertFile = new File(this.caCert);
		File clientCertFile = new File(this.clientCert);
		File clientKeyFile = new File(this.clientKey);

		NettyChannelBuilder builder = NettyChannelBuilder.forAddress(idGeneratorServerAddress, idGeneratorServerPort);

		if (usingTlS) {
			log.info("Using Grpc mutual tsl configuration...");
			return builder
				.sslContext(GrpcSslContexts.forClient()
					.trustManager(caCertFile)
					.keyManager(clientCertFile, clientKeyFile)
					.build())
				.build();
		}
		else {
			log.info("Using Grpc without mutual tsl configuration...");
			return builder.usePlaintext().build();
		}
	}

	@Bean
	public IdGeneratorGrpc.IdGeneratorBlockingStub idGeneratorBlockingStub(ManagedChannel managedChannel) {
		return IdGeneratorGrpc.newBlockingStub(managedChannel);
	}

}
