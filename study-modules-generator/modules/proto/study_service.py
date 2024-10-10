import threading
import grpc
from concurrent import futures
from modules.gsmf_processing.writer import create_module
from . import study_service_pb2, study_service_pb2_grpc


class ModuleServiceServicer(study_service_pb2_grpc.ModuleServiceServicer):
    def CreateModule(self, request, context):
        module_creation_thread = threading.Thread(target=create_module, args=[request])
        module_creation_thread.start()
        return study_service_pb2.ModuleCreationResponse(status="0")


def start_study_grpc_server():
    port = 50051
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    study_service_pb2_grpc.add_ModuleServiceServicer_to_server(ModuleServiceServicer(), server)
    server.add_insecure_port(f'[::]:{port}')
    server.start()
    print(f"INFO:     gRPC server is running on port {port}")
    server.wait_for_termination()
