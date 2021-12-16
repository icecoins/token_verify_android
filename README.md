# TokenVerifier
这个app在初次登录成功后将token储存在本地，并在下次打开时尝试使用token登录

与其配套使用的服务端是基于springboot的token验证器：https://github.com/icecoins/springboot-verify-token-raspi

在build apk之前，请修改 Api.java 中的url，将其指向你所愿的服务器url

App中默认使用https连接，并通过post json直接传输用户名和密码，若欲使用内网服务器或http，请自行修改代码

注意：bug非常多，很有可能一运行就崩溃，请不要过于惊讶
