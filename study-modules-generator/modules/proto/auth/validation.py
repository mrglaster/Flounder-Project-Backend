import grpc

from modules.proto.auth import auth_validator_pb2_grpc, auth_validator_pb2


def validate_jwt_token(token: str) -> bool:
    if not len(token):
        return False
    with grpc.insecure_channel('localhost:9090') as channel:
        stub = auth_validator_pb2_grpc.TokenValidationServiceStub(channel)
        request = auth_validator_pb2.TokenValidateRequest(token='your_token_here')
        try:
            response = stub.ValidateToken(request)
            return True
        except grpc.RpcError as e:
            return False
