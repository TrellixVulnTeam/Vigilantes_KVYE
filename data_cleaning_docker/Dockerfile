FROM public.ecr.aws/lambda/python:3.8




RUN yum update && yum install mesa-libGLU -y \  
    && yum install libgomp -y \
    && python -m pip install boto3 \
    && python -m pip install paddlepaddle==2.1.1   \
    && python -m pip install paddlehub==2.1.0  




ENV HOME /tmp
ENV HUB_HOME /tmp
ENV MODULE_HOME /modules


COPY lambda_function.py  ${LAMBDA_TASK_ROOT}
RUN hub install solov2




