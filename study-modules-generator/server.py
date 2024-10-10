import os
import threading
import uvicorn
from fastapi import FastAPI, HTTPException, Depends
from fastapi.responses import FileResponse
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from modules.proto.auth.validation import validate_jwt_token
from modules.proto.study_service import start_study_grpc_server

app = FastAPI()
DATA_DIR = "data"

security = HTTPBearer()


@app.get("/download/image/{file_name}")
async def download_image(file_name: str):
    file_path = os.path.join(DATA_DIR, "images", file_name)
    if not os.path.exists(file_path):
        raise HTTPException(status_code=404, detail="File not found")
    return FileResponse(path=file_path, filename=file_name, media_type='application/octet-stream')


@app.get("/download/modules/{module_name}")
async def download_module(module_name: str):
    file_path = os.path.join(DATA_DIR, "modules", module_name)
    if not os.path.exists(file_path):
        raise HTTPException(status_code=404, detail="File not found")
    return FileResponse(path=file_path, filename=module_name, media_type='application/octet-stream')


def main():
    consume_thread = threading.Thread(target=start_study_grpc_server)
    consume_thread.start()
    uvicorn.run(f"{os.path.basename(__file__)[:-3]}:app", log_level="info", port=5615, host="192.168.0.121")


if __name__ == '__main__':
    main()


