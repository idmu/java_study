##### 启动与停止
* 启动mysql服务
   ```sudo /usr/local/mysql/support-files/mysql.server start```
* 停止mysql服务
   ```sudo /usr/local/mysql/support-files/mysql.server stop```
* 重启mysql服务
    ```sudo /usr/local/mysql/support-files/mysql.server restart```
* 进入mysql目录文件
  ```cd /usr/local/mysql/support-files```
* 进入mysql命令行
```/usr/local/MySQL/bin/mysql -uroot -p12345678```
* 退出数据库
```exit;```

##### 数据库相关操作
* 查询所有数据库
 ```show databases; ```
* 选择(使用)数据库
```use mybatis;```
* 查询当前正在使用的数据库名称
```select database();```
* 创建数据库
 ```create database 数据库名称; ```
 ```创建数据库,判断不存在,再创建: create database if not exists 数据库名;```
* 删除数据库
```drop database 数据库名称;```
```判断数据库存在，存在再删除:drop database if exists 数据库名称;```

##### 数据库表相关操作
* 创建数据库表

```sql
create table 表名(
	列名1 数据类型1,
	列名2 数据类型2,
	....
	列名n 数据类型n
	);
```

* 复制表
```create table 表名 like 被复制的表名; ```
* 查看某个数据库中的所有的数据表
```show tables;```
* 查看数据表的结构
```desc pet;```或```describe pet;```
* 修改表名
```alter table 表名 rename to 新的表名;```
* 修改表的字符集
```alter table 表名 character set 字符集名称;```
* 添加一列
```alter table 表名 add 列名 数据类型;```
* 删除列
```alter table 表名 drop 列名;```
* 删除表
```drop table 表名;```或```drop table  if exists 表名 ;```
----
* 添加数据
```insert into 表名(列名1,列名2,...列名n) values(值1,值2,...值n);```
其中列名和值要一一对应。如果表名后，不定义列名，则默认给所有列添加值,如:```insert into 表名 values(值1,值2,...值n);```除了数字类型，其他类型需要使用引号(单双都可以)引起来.
* 删除数据
 ```delete from 表名  where 条件 ```
 其中:如果不加条件，则删除表中所有记录。如果要删除所有记录, 使用```delete from 表名; ```一般不推荐使用。这种操作有多少条记录就会执行多少次删除操作.
```TRUNCATE TABLE 表名;```推荐使用，效率更高 先删除表，然后再创建一张一样的表.
* 修改数据
```update 表名 set 列名1 = 值1, 列名2 = 值2,... where 条件;```如果不加任何条件，则会将表中所有记录全部修改.

```sql
insert into user2 values (1,'李四','123'); // 增
delete from pet where ower = 'disn'; //删
update pet set name = '后裔' where ower = 'dfn'; //改

```

* 查询数据

```sql
①> 、< 、<= 、>= 、= 、<>	
②BETWEEN...AND	
③ IN( 集合)	
④LIKE 模糊查询	
⑤_单个任意字符
⑥%多个任意字符
⑦IS NULL  
⑧and  或 &&
⑨or  或 || 
⑩not  或 !
查询条件应用举例:
SELECT * FROM user WHERE age >= 18;
SELECT * FROM user WHERE age >= 18 AND  age <=36;
SELECT * FROM user WHERE age BETWEEN 40 AND 70;
SELECT * FROM user WHERE age IN (6,18,37);
// 关于NULL
SELECT * FROM user WHERE height = NULL; 错误,因为null值不能使用=或（!=) 判断
SELECT * FROM user WHERE height IS NULL;(正确)
SELECT * FROM user WHERE height  IS NOT NULL;(正确)
// 查询姓陈的有哪些？< like>
SELECT * FROM user WHERE NAME LIKE '陈%';
// 查询姓名第二个字是新的人
SELECT * FROM user WHERE NAME LIKE "_新%";
// 查询姓名是三个字的人
SELECT * FROM user WHERE NAME LIKE '___';
// 查询姓名中包含狗的人
SELECT * FROM user WHERE NAME LIKE '%狗%';
```

##### 约束相关
* 主键约束		(primary key)
能够唯一确定一张表中的的一条记录,我们通过给某个字段添加约束, 可以使得这个字段不重复且不为空.

```sql
 create table user (
	id int primary key auto_increment, // 在创建表时，添加主键约束，并且完成主键自增	
	name varchar(20)
 );
-- 联合主键: 由多个字段联合组成的主键, 只要联合的主键加起来不重复就可以.联合主键中的任何一个字段都不能为空.
create table user2 (
 	id int,
 	name varchar(20),
 	password varchar(20),
 	primary key(id, name)
);

```

表创建完成后:
添加主键.如:
①```alter table user add primary key(id);```②```alter table user modify id int primary key;```
删除主键:```alter table user drop primary key;```
* 唯一约束：unique  约束修饰的字段的值不可以重复.

```java
 create table user1 (
 	id int primary key auto_increment,
  	phone_num varchar(20) unique
  	 );
 create table user2 (
 	id int primary key auto_increment,
  	name varchar(20),
  	unique(id, name) // 表示两个字段在一起不重复就可以
  	 );
  	 
```

也可以在表创建完成后, 通过```alter table user3 add unique(phone_num);```或```alter table user3  modify phone_num varchar(20) unique;```来添加unique约束.
删除unique约束:```alter table user3 drop index phone_num;```
* 非空约束：not null  修饰的字段不能为空NULL

```java
create table user3 (
	id int primary key auto_increment,
	name varchar(20) not null
	);
```

删除非空约束:```alter table user3 modify name varchar(20);```
* 默认约束
当我们插入字段值时候,如果对应的字段没有插入值,则会使用默认值.如果传入了值,则不会使用默认值.

```java
create table user4(
	id int primary key auto_increment,
	age int default 18,
	name varchar(20) not null
	);
```

* 外键约束：foreign key

```sql
create table 表名(
....
外键列
constraint 外键名称 foreign key (外键列名称) references 主表名称(主表列名称)
);
// 班级
create table classes(
	id int primary key,
	name varchar(20)
	);	
// 学生表
create table student (
		id	int primary key,
		name varchar(20),
		class_id int,
		foreign key(class_id) references classes(id)
		);
		
```

##### 数据库查询进阶
* 查询所有记录
例如:查询student表中的所有记录.
```select * from student;```
* 查询指定字段
例如:查询student中的sname,ssex,class.
```select sname,ssex,class from student;```
* 查询教师表中所有的单位即不重复的depart列. <排除重复distinct>
```select distinct depart from teacher;```
* 查询score表中成绩在60到80之间的所有记录 <查询区间 between...and...>
```select * from  score where degree between 60 and 80;```
```select * from score where degree > 60 and degree < 80;```
* 查询score表中成绩为85,86或88的记录
```select * from score where degree in(85, 86, 88);```
* 查询student表中'95031'班或性别为'女'的同学记录. <or  表示或者>
```select *from student where class = '95031' or sex = '女';```
* 以class降序查询student表的所有记录 <降序:desc, 升序asc,默认升序(省略)>.
```select * from student  order by class desc;```
* 以cno升序,degree降序查询score表的所有记录
```select * from score order by cno asc,degree desc;```
* 查询"95031'班的学生人数 <统计 count>
```select count(*) from student where class = '95031';```
* 查询score表中最高分的学生学号和课程号(子查询)
```select sno, cno from score where degree = (select  max(degree) from score );```其中:select  max(degree) from score 先查出最高分.
```select sno,cno degree from score order by degree desc limit 0,1;```其中:limit第一个数字表示从多少开始,第二个表示多少条.当有多个相同最高分时,容易出bug,不推荐使用这种方式查询.
* 查询每门课的平均成绩
```select cno, avg(degree) from score group by cno;```
* 查询score表中至少有2名学生选修的并以3开头的课程的平均分数.
```select cno, avg(degree) from score group by cno having count(cno) >= 2 and cno like '3%';```
* 查询分数大于70, 小于90的sno列.
```select sno, degree  from score  where  degree between 70 and 90;```
 * 查询所有学生的sname, cno和degree列.
 ```select sname, cno, degree from student, score where student.sno = score.sno; ```
 * 查询所有学生的sno,cname和degree列
```select sno,cname,degree from course ,score where course.cno = score.cno;```
* 查询"95031"班学生每门课的平均分.
```select cno, avg(degree) from score where sno in (select sno from student where class = '95031') group by cno;```
* 查询选修"3-105"课程的成绩高于"109"号同学"3-105"成绩的所有同学的记录.
```select * from score where cno = '3-105' and  degree > (select degree from score where sno = '109' and  cno = '3-105');```
* 查询成绩高于学号为"109", 课程号为"3-105"的成绩的所有记录
```select * from score where degree > (select degree from score where sno = '109' and cno = '3-105');```
* 查询和学号为108,101的同学同年出生的所有的sno, sname, sbirthday
```select *from student where year(sbirthday) in (select year(sbirthday) from student where sno in(108, 101));```
* 查询"张旭"教师任课的学生成绩
```select * from score where cno = ( select cno from course where tno = (select tno from teacher where tname = "张旭"));```
* 查询选修某课程的同学人数多于5人的教师姓名.
```select tname from teacher where tno = (select tno from course where cno = (select cno from score group by cno having count(*) > 5));```
* 查询存在有85分以上的成绩的课程的cno
```select cno, degree from score where degree > 85;```
* 查询出"计算机系"教师所教课程的成绩表
```select * from score where cno in (select cno from course where tno in (select tno from teacher where depart = "计算机系"));```
* 查询选修编号为"3-105"课程且成绩至少高于选休息编号为"3-245"的同学的cno,sno和degree,并按degree从高到低次序排序.
```any 至少一个.```
```sql
select * from score where cno = '3-105' and degree > any(select degree from score where cno = '3-245') order by degree desc;

```
* 查询选修编号为"3-105"课程且成绩高于选休息编号为"3-245"的同学的cno,sno和degree,并按degree从高到低次序排序.
```all 表示所有```
```sql
select * from score where cno = '3-105' and degree > all(select degree from score where cno = '3-245') order by degree desc;

```
* 查询所有教师和同学的name, sex和birthday
```sql
select tname as name, tsex as sex, tbirthday as birthday from teacher union select sname, ssex, sbirthday from student;

```

* 查询所有"女"教师和"女"同学的name,sex和birthday
```sql
select tname as name, tsex as sex, tbirthday as birthday from teacher where tsex = '女' union select sname, ssex, sbirthday from student where ssex = '女';


```

* 查询成绩比该课程成绩低的同学的成绩表
思路: 从a表查出对应的分数跟b表筛选出来的平均分作比较.
```sql
select * from score a where degree < (select avg(degree) from score b where a.cno = b.cno);
表a
+-----+-------+--------+
| sno | cno   | degree |
+-----+-------+--------+
| 101 | 3-105 |     91 |
| 102 | 3-105 |     92 |
| 103 | 3-105 |     92 |
| 103 | 3-245 |     86 |
| 103 | 6-166 |     85 |
| 104 | 3-105 |     81 |
| 105 | 3-105 |     88 |
| 105 | 3-245 |     75 |
| 105 | 6-166 |     79 |
| 109 | 3-105 |     76 |
| 109 | 3-245 |     68 |
| 109 | 6-166 |     81 |
+-----+-------+--------+
12 rows in set (0.00 sec)   

表b
| sno | cno   | degree |
+-----+-------+--------+
| 101 | 3-105 |     91 |
| 102 | 3-105 |     92 |
| 103 | 3-105 |     92 |
| 103 | 3-245 |     86 |
| 103 | 6-166 |     85 |
| 104 | 3-105 |     81 |
| 105 | 3-105 |     88 |
| 105 | 3-245 |     75 |
| 105 | 6-166 |     79 |
| 109 | 3-105 |     76 |
| 109 | 3-245 |     68 |
| 109 | 6-166 |     81 |
+-----+-------+--------+
12 rows in set (0.00 sec) 

```

* 查询所有任课教师的tname和depart
```select tname, depart from teacher where tno in (select tno from course);```
* 查询至少有两名男生的班号

```sql
select class from student where ssex= '男' group by class having count(*) > 1

```
* 查询student表中不姓"王"的同学记录

```sql
select * from student where sname not like '王%';
```

* 查询student表中每个学生的姓名和年龄

```sql
select sname, year(now()) - year(sbirthday)  as '年龄' from student;
```

* 查询student表中最大和最小的sbirthday日期值

```sql
select max(sbirthday) as '最大', min(sbirthday) as '最小' from student;
```
* 以班号和年龄从大到小的顺序查询student表中的全部记录

```sql
select * from student order by class desc, sbirthday;
```

* 查询"男"教师及其所上的课程

```sql
select * from course where tno in (select tno from teacher where tsex = '男');

```
* 查询最高分同学的sno, cno和degree列
```sql
select * from score where degree = (select max(degree) from score);
```
* 查询和李军同性别的所有同学的sname
```sql
select sname from student where ssex = (select ssex from student where sname = '李军');

```
* 查询和李军同性别并同班 同学sname
```sql
select sname from student where ssex = (select ssex from student where sname = "李军") and class = (select class from student where sname = '李军');

```
* 查询所有选修"计算机导论"课程的"男"的成绩表

```sql
select * from score where cno = (select cno from course where cname = '计算机导论') and sno in(select sno from student where ssex = '男');

```

##### SQL的四种连接查询
```sql
分析用例的数据准备:
mysql> select * from person;
+----+--------+--------+
| id | name   | cardId |
+----+--------+--------+
|  1 | 张三   |      1 |
|  2 | 李四   |      3 |
|  3 | 王五   |      6 |
+----+--------+--------+
3 rows in set (0.00 sec)
mysql> select * from card;
+------+-----------+
| id   | name      |
+------+-----------+
|    1 | 饭卡      |
|    2 | 建行卡    |
|    3 | 农行卡    |
|    4 | 工商卡    |
|    5 | 邮政卡    |
+------+-----------+
5 rows in set (0.00 sec)
```

* 内连接
 ```inner  join 或者 join```, ```后面通常跟对一个on表示条件```
   ----   内联查询: 就是两张表中的数据, 通过某个字段相等,查询出相关记录数据.
   <当前表中的cardid与id相同.>
```sql
select * from person inner join card on person.cardId = card.id;
+----+--------+--------+------+-----------+
| id | name   | cardId | id   | name      |
+----+--------+--------+------+-----------+
|  1 | 张三   |      1 |    1 | 饭卡      |
|  2 | 李四   |      3 |    3 | 农行卡    |
+----+--------+--------+------+-----------+
2 rows in set (0.00 sec)

```
 * 外连接
```左外连接:左连接 left join 或者 left outer join```
 ---- 左外连接, 会把左边表里面的所有数据取出来, 而右边表中的数据,如果有相等的,就显示出来, 如果没有, 则会补NULL.

```sql
select * from person left join card on person.cardId = card.id;

+----+--------+--------+------+-----------+
| id | name   | cardId | id   | name      |
+----+--------+--------+------+-----------+
|  1 | 张三   |      1 |    1 | 饭卡      |
|  2 | 李四   |      3 |    3 | 农行卡    |
|  3 | 王五   |      6 | NULL | NULL      |
+----+--------+--------+------+-----------+
3 rows in set (0.00 sec)

```

```右外连接:右连接 right join 或者right outer join```
----右外连接, 会把右边表里面的所有数据取出来, 而左边表中的数据,如果有相等的,就显示出来, 如果没有, 则会补NULL.

```sql
select * from person right join card on person.cardId = card.id;

+------+--------+--------+------+-----------+
| id   | name   | cardId | id   | name      |
+------+--------+--------+------+-----------+
|    1 | 张三   |      1 |    1 | 饭卡      |
|    2 | 李四   |      3 |    3 | 农行卡    |
| NULL | NULL   |   NULL |    2 | 建行卡    |
| NULL | NULL   |   NULL |    4 | 工商卡    |
| NULL | NULL   |   NULL |    5 | 邮政卡    |
+------+--------+--------+------+-----------+
5 rows in set (0.01 sec)

```
```全外连接:完全外连接 full join 或者full outer join```<mysql不支持full join>

```sql
mysql> select * from person full join card on person.cardId= card.id;
ERROR 1054 (42S22): Unknown column 'person.cardId' in 'on clause'
**** 解决mysql不支持full join的方法****
 <左连接 + 右链接> , 即通过union来连接左右连接. <左连接 union 右链接>.
eg:

select * from person left join card on person.cardId = card.id union select * from person right join card on person.cardId = card.id;

+------+--------+--------+------+-----------+
| id   | name   | cardId | id   | name      |
+------+--------+--------+------+-----------+
|    1 | 张三   |      1 |    1 | 饭卡      |
|    2 | 李四   |      3 |    3 | 农行卡    |
|    3 | 王五   |      6 | NULL | NULL      |
| NULL | NULL   |   NULL |    2 | 建行卡    |
| NULL | NULL   |   NULL |    4 | 工商卡    |
| NULL | NULL   |   NULL |    5 | 邮政卡    |
+------+--------+--------+------+-----------+
6 rows in set (0.01 sec)

```

 ##### 要点梳理
* where 和 having 的区别？
(1)   having通常用在聚合函数前面，对聚合函数进行过滤,（MAX、MIN、COUNT、SUM).having通常和group by 一起连用，因为where不能加在group by的后面.
 (2) where 在分组之前进行限定，如果不满足条件，则不参与分组。having在分组之后进行限定，如果不满足结果，则不会被查询出来. where 后不可以跟聚合函数，having可以进行聚合函数的判断。

```sql
MYSQL执行语句顺序，严格遵循次顺序，不能改变
select
from
where
group by
having
order by
```

##### mysql的事务
* 关于事务
 mysql中, 事务其实是一个最小的不可分割的工作单元. 事务能够保证一个业务的完整性.
分析:
```sql
例如:
a --> -100
update user set money = money - 100 where name = 'a';
b --> +100
update user set money = money + 100 where name = 'b';
-- 实际程序中, 如果只有一条sql语句执行成功了,而另外一条没有执行成功?则会出现前后数据不一致的情况.
update user set money = money - 100 where name = 'a';
update user set money = money + 100 where name = 'b';
在多条sql语句,可能会有同时成功的要求,要么就同时失败.

```
* 事务控制
(1)事务主要包含自动提交```@@autocommit=1;```,手动提交```commit;```和事务回滚```rollback;```.
	 	(2) mysql默认是开启事务的(自动提交).
 ----当我们去执行一个sql语句的时候,效果会立即提现出来,且不能回滚.
 ```set autocommit = 0;设置mysql是否自动提交,<0为否, 1为是.>```
 ```select @@autocommit;查看mysql的自动提交方式.```
 ```commit; 手动提交.```
 具体事务控制相关参照下面代码分析:
```sql
mysql> select @@autocommit;
+--------------+
| @@autocommit |
+--------------+
|            1 |
+--------------+
1 row in set (0.00 sec)
// 建表
create database bank;
create table user (
	id int primary key,
    name varchar(20),
    money int
    );
// 首先在表中插入一条用户数据a.
insert into user values (1,'a',1000);
Query OK, 1 row affected (0.00 sec)
// 进行回滚操作.
mysql> rollback;
Query OK, 0 rows affected (0.00 sec)
// 执行回滚后,查看数据表信息,发现即使调用了rollback,但插入的数据依然存在.说明当前不能回滚.
mysql> select * from user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
+----+------+-------+
1 row in set (0.00 sec)
// 可以通过设置msql的回滚自动提交为false.
set autocommit = 0;
Query OK, 0 rows affected (0.00 sec)
mysql> select @@autocommit;
+--------------+
| @@autocommit |
+--------------+
|            0 |
+--------------+
1 row in set (0.00 sec)
// 也就说, 通过上面的set autocommit = 0;操作关闭了mysql的自动提交(commit).
*******再次插入数据:*******
insert into user values (2,'b',1000);
Query OK, 1 row affected (0.00 sec)
// 插入数据后查看表,用户2数据添加成功.
mysql> select * from user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
|  2 | b    |  1000 |
+----+------+-------+
2 rows in set (0.00 sec)
// 执行回滚操作.
mysql> rollback;
Query OK, 0 rows affected (0.00 sec)
// 回滚后再次查看表,发现刚才插入的数据已经被干掉了.
mysql> select * from user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
+----+------+-------+
1 row in set (0.01 sec)
**** 对于这种场景,如果想让用户b数据成功提交, 可以通过commit;命令执行手动提交操作.手动提交后,如果想再次通过rollback来撤销,则是不可以的.也就是说,事务一旦提交,执行的sql语句就不可以再撤销,也就是说事务一旦提交数据就会持久的产生效果.



```
(3)手动开启事务
```begin```和```start transaction```都可以手动开启一个事务. 也就是说,当我们当前的mysql如果默认的是自动提交模式,则执行rollback进行事务回滚则是无效的. 但是可以通过```begin```和```start transaction```手动开启事务.

```sql
即:
  当前默认为自动提交模式,此时执行rollback无效.执行下面sql语句:
  start transaction;(或者begin;)
  update user set money = money - 100 where name = 'a';
  update user set money = money + 100 where name = 'b';
  执行完插入a,b用户数据后,再执行rollback,发现可以成功回滚事务.可以成功切换成手动开启事务的模式.若想使得插入的数据生效,也需要手动执行commit进行提交操作.
  事务开启之后,一旦commit提交,就不可以回滚,也就说,当前的这个事务在提交的时候就已经结束了.
  
```
* 事务的四大特征 <ACID>
A 原子性: 事务是最小的单元, 不可以在分割.
C 一致性: 事务要求, 同一事务中的sql语句必须保证同时成功,同时失败.
I 隔离性: 事务1 和事务2之间shi具有隔离性的.
D 持久性: 事务一旦结束(commit,rollback),就不可以返回.
* 事务的隔离性
多个事务之间隔离的，相互独立的。但是如果多个事务操作同一批数据，则会引发一些问题，设置不同的隔离级别就可以解决这些问题.
存在问题：
(1) 脏读：一个事务，读取到另一个事务中没有提交的数据.
(2)不可重复读(虚读)：在同一个事务中，两次读取到的数据不一样.
(3)幻读：一个事务操作(DML)数据表中所有记录，另一个事务添加了一条数据，则第一个事务查询不到自己的修改.
	```read uncommitted;	读未提交的```-->产生的问题：脏读、不可重复读、幻读.
	```read committed;		读已经提交的```-->产生的问题：不可重复读、幻读```repeatable read;	可以重复读```-->产生的问题：幻读
	```serializable;		串行化```<性能特差>
通常是隔离级别越高,性能越差.
(1)查看数据库的隔离级别
mysql默认的隔离级别: REPEATABLE-READ  
mysql8.0:
 系统级别的:```select @@global.transaction_isolation;```
 会话级别的:```select @@transaction_isolation;```
mysql5.x: 
 系统级别的:```select @@global.tx_isolation;```
 会话级别的:```select @@tx_isolation;```
```sql
mysql> select @@global.transaction_isolation;
+--------------------------------+
| @@global.transaction_isolation |
+--------------------------------+
| REPEATABLE-READ                |
+--------------------------------+
1 row in set (0.00 sec)
```
  (2)修改隔离级别
  ```set global tansaction isolation level read uncomitted;```


##### 数据库的三大范式
* 第一范式
* 数据表中的所有字段都是不可分割的原子项.初步可以理解为:字段值还可以继续拆分的,就不满足第一范式.
比如某表中有一个```address```的字段,插入值为"中国陕西省西安市碑林区柏树林11号".该字段值是可以继续拆分的,原则上就不满足第一范式.可以依次拆分为:国家/省/市/区/街道等等.
当然,范式设计的越详细,对某些实际操作可能会更好.但不一定都是好处.<比如对address字段来说,可能拆分开来永远都用不到这么详细的信息,可能就没有拆分的必要.>
* 第二范式
必须是满足第一范式的前提下,第二范式要求,除主键外的每一列都必须完全依赖主键.如果要出现不完全依赖,只可能发生在联合主键的情况下.

```sql
例如:
create table myorder(
		product_id int,
		customer_id int,
		product_name varchar(20),
		customer_name varchar(20),
		primary key(product_id, customer_id
	);
	当前表中, 除主键以外的其他列, 只依赖于主键的部分字段.则不满足第二范式,通常需要拆表.
create table myorder(
		order_id int primary key,
		product_id int,
		customer_id int
	);
create  table product (
		id int primary key,
		name varchar(20)
	);
create table customer(
		id int primary key,
		name varchar(20)
		);
拆分成三个表后,满足第二范式.
```

* 第三范式
 必须先满足第二范式.除开主键列的其他列之间不能有传递依赖关系.



##### 附件
查询语句所涉及的sql语句
```sql
create table student(
	sno varchar(20) primary key,
	sname varchar(20) not null,
	ssex varchar(20) not null,
	sbrithday datetime,
	class varchar(20)
	);

create table student(
	sno varchar(20) primary key,
	sname varchar(20) not null,
	ssex varchar(10) not null,
	sbirthday datetime,
	class varchar(20)
)

create table teacher(
	tno varchar(20) primary key,
	tname varchar(20) not null,
	tsex varchar(20) not null,
	tbirthday datetime,
	prof varchar(20) not null,
	depart varchar(20) not null
	);

create table course(
	cno varchar(20) primary key,
	cname varchar(20) not null,
	tno varchar(20) not null,
	foreign key(tno) references teacher(tno)
	);

create table score(
	sno varchar(20) not null,
	degree decimal,
	primary key (sno, cno),
	foreign key (sno) references student(sno),
	foreign key (cno) references course(cno)
	);

insert into student values ('101','曾华','男','1977-09-01','95033');
insert into student values ('102','匡明','男','1975-10-02','95031');
insert into student values ('103','王丽','女','1976-01-23','95033');
insert into student values ('104','李军','男','1976-02-20','95033');
insert into student values ('105','王芳','女','1975-02-10','95031');
insert into student values ('106','陆君','男','1974-06-03','95031');
insert into student values ('107','王尼玛','男','1976-02-20','95033');
insert into student values ('108','张全蛋','男','1975-02-10','95031');
insert into student values ('109','赵铁柱','男','1974-06-03','95031');

insert into teacher values ('804','李成','男','1958-12-02','副教授','计算机系');
insert into teacher values ('856','张旭','男','1969-03-12','讲师','电子工程系');
insert into teacher values ('825','王萍','女','1972-05-05','助教','计算机系');
insert into teacher values ('831','刘冰','女','1977-08-14','助教','电子工程系');

insert into course values ('3-105','计算机导论', '825');
insert into course values ('3-245','操作系统', '804');
insert into course values ('6-166','数字电路', '856');
insert into course values ('9-888','高等数学', '831');
  
insert into score values('103','3-245','86');
insert into score values('105','3-245','75');
insert into score values('109','3-245','68');
insert into score values('103','3-105','92');
insert into score values('105','3-105','88');
insert into score values('109','3-105','76');
insert into score values('103','3-105','64');
insert into score values('105','6-166','79');
insert into score values('109','6-166','81');


create table person(
	id int primary key auto_increment,
	name varchar(20),
	cardId int
);

create table card (
	id int,
	name varchar(20)
);

insert into card values (1,'饭卡');
insert into card values (2,'建行卡');
insert into card values (3,'农行卡');
insert into card values (4,'工商卡');
insert into card values (5,'邮政卡');

insert into person values (1,'张三',1);
insert into person values (2,'李四',3);
insert into person values (3,'王五',6);



```