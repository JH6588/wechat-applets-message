# wechat-applets-message
批量微信小程序access_token 统一管理， 自动整合自动发送客服消息任务(也可方便的整合其他微信接口）。

- 通过脚本定时去读源数据（我现在是google sheet , 但也可以是数据库，txt) 根据AppToken对象来生成json串 同步到redisTemplate。  这样方便后面切换源数据。
