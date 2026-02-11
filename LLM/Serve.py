from fastapi import FastAPI
from langserve import add_routes
from langchain_core.runnables import RunnableLambda
import os
from dotenv import load_dotenv
load_dotenv()

langserve_port = int(os.getenv("LANGSERVE_PORT"))

from Agent import workflow
from Dto import AgentInput, AgentOutput

app = FastAPI()

base_app = workflow()

# answer 상태만 추출
answer_only = RunnableLambda(lambda state: {"answer": state["answer"]})

graph_app = (base_app | answer_only).with_types(
    input_type=AgentInput,
    output_type=AgentOutput,
)

add_routes(
    app,
    graph_app,
    path='/chat'
)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host='0.0.0.0', port=langserve_port)