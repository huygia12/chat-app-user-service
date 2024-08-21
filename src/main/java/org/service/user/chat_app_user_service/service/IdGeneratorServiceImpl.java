package org.service.user.chat_app_user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.IdGeneratorGrpc;
import proto.IdGeneratorOuterClass;

import java.math.BigInteger;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

	@Autowired
	IdGeneratorGrpc.IdGeneratorBlockingStub idGeneratorBlockingStub;

	public BigInteger generateID() {
		IdGeneratorOuterClass.GenerateIdResponse response = idGeneratorBlockingStub
			.generateId(IdGeneratorOuterClass.GenerateIdRequest.newBuilder().build());
		return BigInteger.valueOf(response.getId());
	}

}
