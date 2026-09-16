####################
# ocpp16-deamon
####################
health check : 8090 포트
http://127.0.0.1:8090/checkHealth
응답(JSON) status:OK 

서비스 포트 : 8091 (mtls)
####################
# proxy-eai
####################
health check : 9091 포트
http://127.0.0.1:9091/checkHealth
응답(JSON) status:OK 

####################
# api-eai
####################
health check : 9092 포트
http://127.0.0.1:9092/checkHealth
응답(JSON) status:OK 
