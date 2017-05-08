drop table userimport;

create table userimport(
id int  not null primary key,
name varchar(20) not null,
age int not NULL,
address varchar(20) not null,
hight int not null);





tcpdump tcp -i eth0  port 3306  -w ./mysql_loaddata_success_pkg.cap


load data infile "/home/mark/Orders txt" replace into table Orders fields terminated by',' enclosed by '"';



load data LOCAL infile "D:/java/test/data1.csv" replace into table userimport fields terminated by ',' (id,name,age,address,hight);


mysql -uroot -pliujun  db1 --local-infile=1 -e 'load data infile "/home/mycat1/data/data1.csv" replace into table userimport fields terminated by ',' lines terminated by '/n''     