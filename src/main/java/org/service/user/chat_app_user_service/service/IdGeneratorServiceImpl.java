package org.service.user.chat_app_user_service.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.IdGeneratorGrpc;
import proto.IdGeneratorOuterClass;

import java.math.BigInteger;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

	@GrpcClient("id-generator-service")
	IdGeneratorGrpc.IdGeneratorBlockingStub idGeneratorBlockingStub;

	public BigInteger generateID() {
		IdGeneratorOuterClass.GenerateIdResponse response = idGeneratorBlockingStub
			.generateId(IdGeneratorOuterClass.GenerateIdRequest.newBuilder().build());
		return new BigInteger(response.getId());
	}

}
