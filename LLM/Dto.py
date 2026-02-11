from pydantic import BaseModel

class AgentInput(BaseModel):
    question: str
    one_way: bool = True

class AgentOutput(BaseModel):
    answer: str