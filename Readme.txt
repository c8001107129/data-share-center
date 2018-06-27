1、执行程序的时候选择配置文件
java -jar gts2-bi-1.0-SNAPSHOT.jar --spring.profiles.active=prd

java -jar ../lib/gts2-bi-1.0-SNAPSHOT.jar --server.port=68905

2、后台运行程序
进入bin目录执行
./startDataShareCenter.sh start|stop|restart


3、初始化操作
创建索引：
use gts2_business_data
db.gts2_t_clone_custrans_real.createIndex({"object.company_id":1})
db.gts2_t_clone_order_real.createIndex({"object.company_id":1})
db.gts2_t_clone_position_real.createIndex({"object.company_id":1})
db.gts2_t_clone_trade_real.createIndex({"object.company_id":1})




#创建权限配置db与集合
use permission_config
#插入权限配置
#user=用户名、pwd=密码、collectionName=集合名称、limit=限制记录数、condition=限制条件
db.gts2_config.insert({user:'test', pwd:'test', collectionName:'gts2_t_clone_custrans_real', limit:100000, condition:{'object.company_id': 5}})
db.gts2_config.insert({user:'test', pwd:'test', collectionName:'gts2_t_clone_order_real', limit:100000, condition:{'object.company_id': 5}})
db.gts2_config.insert({user:'test', pwd:'test', collectionName:'gts2_t_clone_position_real', limit:100000, condition:{'object.company_id': 5}})
db.gts2_config.insert({user:'test', pwd:'test', collectionName:'gts2_t_clone_trade_real', limit:100000, condition:{'object.company_id': 5}})

db.gts2_config.insert({user:'test1', pwd:'test1', collectionName:'gts2_t_clone_custrans_real', limit:100000, condition:{'object.company_id': 1}})
db.gts2_config.insert({user:'test1', pwd:'test1', collectionName:'gts2_t_clone_order_real', limit:100000, condition:{'object.company_id': 1}})
db.gts2_config.insert({user:'test1', pwd:'test1', collectionName:'gts2_t_clone_position_real', limit:100000, condition:{'object.company_id': 1}})
db.gts2_config.insert({user:'test1', pwd:'test1', collectionName:'gts2_t_clone_trade_real', limit:100000, condition:{'object.company_id': 1}})


db.gts2_config.insert({user:'test8', pwd:'test8', collectionName:'gts2_t_clone_custrans_real', limit:100000, condition:{'object.company_id': 8}})
db.gts2_config.insert({user:'test8', pwd:'test8', collectionName:'gts2_t_clone_order_real', limit:100000, condition:{'object.company_id': 8}})
db.gts2_config.insert({user:'test8', pwd:'test8', collectionName:'gts2_t_clone_position_real', limit:100000, condition:{'object.company_id': 8}})
db.gts2_config.insert({user:'test8', pwd:'test8', collectionName:'gts2_t_clone_trade_real', limit:100000, condition:{'object.company_id': 8}})
db.gts2_config.insert({user:'test8', pwd:'test8', collectionName:'gts2_t_clone_cashin_proposal_real', limit:100000, condition:{'object.company_id': 8}})


4、增加开机自启动脚本（Centos7）
4.1、首先，脚本具有可执行权限
chmod +x /opt/data-share-center-1.0/bin/startDataShareCenter.sh
4.12、然后将脚本存放的绝对路径+脚本全名追加到/etc/rc.d/rc.local文件最后
/opt/data-share-center-1.0/bin/startDataShareCenter.sh restart #springboot服务
4.13、在centos7中，/etc/rc.d/rc.local的权限被降低了，所以需要执行如下命令赋予其可执行权限
chmod +x /etc/rc.d/rc.local

4.2 设置监控
*/1 * * * * /opt/data-share-center-1.0/bin/dataShareCenterCheck.sh >> /opt/data-share-center-1.0/logs/check.log

4.3 网络流量监控
nohup /opt/data-share-center-1.0/bin/net.sh ens160 >/dev/null 2>&1 &

5、curl 模拟请求
--data-urlencode 对某些条件进行编码
curl -v -L -G --data-urlencode 'condition={"object.trade_time": {"$gt": "2018-05-01 00:00:00"}}' "http://192.168.35.44:8088/api/gts2/find?user=test1&pwd=test1&collectionName=gts2_t_clone_trade_real&limit=10"

6、此功能移除
新增参数 read_num，一次查询多次读取的数据量。如设置则是按照下面的需求分批读取，如不设置则全量读取。
使用例子：read_num=500
1. 第一次请求查询数据，数据量2000。则返回 1-500 的数据，同时返回总条数(all_num)，和当前读取页(page_num)，和具体内容。
2. 第二次请求查询数据（和第一次一样参数不变），数据量20000。则返回 501-1000 的数据，同时返回总条数(all_num)，和当前读取页(page_num)，和具体内容。
3. 第三次请求查询数据（和第一次一样参数不变），数据量20000。则返回 1001-1500 的数据，同时返回总条数(all_num)，和当前读取页(page_num)，和具体内容。
4. 以此类推。


