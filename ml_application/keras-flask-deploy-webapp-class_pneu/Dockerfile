FROM tensorflow/tensorflow:2.5.1

COPY req_info.txt /
RUN python3 -m pip install -r /req_info.txt

COPY . /app
WORKDIR /app

EXPOSE 5000
CMD [ "python" , "app.py"]
