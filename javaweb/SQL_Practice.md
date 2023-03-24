```sql
drop TABLE if EXISTS student;
CREATE TABLE student (
    id INT(10) PRIMARY key,
    name VARCHAR (10),
    age INT (10) NOT NULL,
    gander varchar(2)
);

drop TABLE if EXISTS course;
CREATE TABLE course (
  id INT (10)  PRIMARY key,
  name VARCHAR (10) ,
  t_id INT (10) 
) ;

drop TABLE if EXISTS teacher;
CREATE TABLE teacher(
  id INT (10)  PRIMARY key,
  name VARCHAR (10) 
);

drop TABLE if EXISTS scores;
CREATE TABLE scores(
  s_id INT ,
  score INT (10),
  c_id INT (10) ,
	PRIMARY key(s_id,c_id)
) ;
```

1、查询‘01’号学生的姓名和各科成绩。 难度：两颗星

```sql
select s.id,s.name sname,c.name cname,r.score from student s
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id
where s.id = 1;
```

2、查询各个学科的平均成绩，最高成绩。 难度：两颗星

```
select c.id,c.name,avg(r.score),max(r.score) from course c
left join scores r on c.id = r.c_id 
group by c.id,c.name;
```

3、查询每个同学的最高成绩及科目名称。 难度：四颗星

```
select t.id,t.name,c.id,c.name,r.score from
(select s.id,s.name,(
	select max(score) from scores r where r.s_id = s.id
) score from student s)  t 
left join scores r on r.s_id = t.id and r.score = t.score
left join course c on r.c_id = c.id;
```

4、查询所有姓张的同学的各科成绩。 难度：两颗星

```
select s.id,s.name sname,c.name cname,r.score from student s
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id
where s.name like '张%';
```

5、查询每个课程最高分的同学信息。 难度：五颗星

```
select * from student s where id in
(
    select distinct r.s_id from
        (
        select c.id,c.name,max(score) score from student s
        left join scores r on r.s_id = s.id
        left join course c on c.id = r.c_id 
        group by c.id,c.name
        ) t 
    left join scores r on t.id = r.c_id and t.score = r.score
);
```

6、查询名字中含有“张”和‘李’字的学生信息和各科成绩 。 难度：两颗星

```
select s.id,s.name sname,c.name cname,r.score from student s
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id
where s.name like '%张%' or s.name like '%李%';
```

7、查询平均成绩及格的同学的信息。 难度：三颗星

```
select * from student s where id in (
  select r.s_id from scores r 
	group by r.s_id 
	having avg(r.score)>60
);
```

8、将学生按照总分数进行排名。 难度：三颗星

```
select s.id,s.name sname,sum(r.score) score from student s
left join scores r on r.s_id = s.id
group by s.id,s.name order by score desc;
```

9、查询数学成绩的最高分、最低分、平均分。 难度：两颗星

```
select c.name,max(score),min(score),avg(score) from course c
left join scores r on c.id = r.c_id
where c.name = '数学';
```

10、将各科目按照平均分排序。 难度：两颗星

```
select c.id,c.name,avg(score) score from course c
left join scores r on c.id = r.c_id
group by c.id,c.name order by score desc;
```

11、查询老师的信息和他所带科目的平均分。 难度：三颗星

```
select t.id,t.name,c.id,c.name,avg(r.score) 
from teacher t
left join course c on t.id = c.t_id 
left join scores r on r.c_id = c.id
group by t.id,t.name,c.id,c.name;
```

12、查询被“张楠”和‘‘李子豪’教的课程的最高分和平均分。 难度：三颗星

```
select t.id,t.name,c.id,c.name,avg(r.score) 
from teacher t
left join course c on t.id = c.t_id 
left join scores r on r.c_id = c.id
group by t.id,t.name,c.id,c.name
having t.name in ('张楠','李子豪');
```

13、查询每个同学的最好成绩的科目名称。 难度：五颗星

```
select t.id,t.sname,r.c_id,c.id,c.name,t.score  from
(select s.id,s.name sname,max(r.score) score 
from student s
left join scores r on r.s_id = s.id
group by s.id,s.name) t
left join scores r on r.s_id = t.id and r.score = t.score
left join course c on r.c_id = c.id ;
```

14、查询所有学生的课程及分数。 难度：一颗星

```
select s.id,s.name sname,c.id,c.name cname,r.score from student s
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id;
```

15、查询课程编号为1且课程成绩在60分以上的学生的学号和姓名。 难度：两颗星

```
select * from student s where id in
(
	select r.s_id from scores r where r.c_id = 1 and r.score > 60
);
```

16、查询平均成绩大于等于85的所有学生的学号、姓名和平均成绩。 难度：三颗星

```
select s.id,s.name,t.score from student s 
left join (
	select r.s_id ,avg(r.score) score from scores r group by r.s_id 
) t on s.id = t.s_id;
```

17、查询有不及格课程的同学信息。 难度：四颗星

```
-- 什么叫有不及格 ---》最低分数的科目如果不及格
select * from student s where id in (
	select r.s_id from scores r group by r.s_id 
	HAVING min(r.score) < 60
);
```

18、求每门课程的学生人数。 难度：两颗星

```
select c.id,c.name, t.number from course c
left join 
(select r.c_id,count(*) number from scores r group by r.c_id) t
on c.id = t.c_id;


select c.id,c.name,count(*) from course c 
left join scores r on c.id = r.c_id
group by c.id,c.name;
```

19、查询每门课程的平均成绩，结果按平均成绩降序排列，平均成绩相同时，按课程编号升序排列。 难度：两颗星

```
select c.id,c.name,avg(score) score from course c 
left join scores r on c.id = r.c_id 
group by c.id,c.name 
order by score desc,c.id asc;
```

20、查询平均成绩大于等于60分的同学的学生编号和学生姓名和平均成绩。 难度：三颗星

```
select s.id,s.name,t.score from student s 
right join (
	select r.s_id,avg(score) score from scores r 
	group by r.s_id having score >= 70
) t on s.id = t.s_id;


select s.id,s.name sname, avg(r.score) score from student s
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id
group by s.id,s.name having avg(r.score) > 70;
```

21、查询有且仅有一门课程成绩在90分以上的学生信息； 难度：三颗星

```
select * from student s where id in (
	select r.s_id from scores r where r.score > 90
	group by r.s_id having count(*) = 1
);

select s.id,s.name,s.gander from student s 
left join scores r on s.id = r.s_id 
where r.score > 90
group by s.id,s.name,s.gander having count(*) = 1;
```

22、查询出只有三门课程的全部学生的学号和姓名。难度：三颗星

```
select * from student s where id in (
	select r.s_id from scores r group by r.s_id having count(*) = 3
);

select s.id,s.name,s.gander from student s 
left join scores r on s.id = r.s_id 
group by s.id,s.name,s.gander having count(*) = 3;
```

23、查询有不及格课程的课程信息 。 难度：三颗星

```
select * from course c where id in (
	select r.c_id from scores r group by r.c_id
	HAVING min(r.score) < 60
);

select r.c_id,c.name from course c
left join scores r on c.id = r.c_id
group by r.c_id,c.name HAVING min(r.score) < 60;
```

24、检索至少选修5门课程的学生学号。难度：三颗星

```
select * from student s where s.id in (
	select r.s_id from scores r group by r.s_id having count(*) >= 5
);

select s.id,s.name from student s 
left join scores r on s.id = r.s_id
group by s.id,s.name having count(*) >= 5;
```

25、查询没有学全所有课程的同学的信息 。难度：四颗星

```
select s.id,s.name,count(*) number from student s 
left join scores r on s.id = r.s_id
group by s.id,s.name having number < (
	select count(*) from course
);
```

26、查询学全所有课程的同学的信息。难度：四颗星

```
select s.id,s.name,count(*) number from student s 
left join scores r on s.id = r.s_id
group by s.id,s.name having number = (
	select count(*) from course
);
```

27、 查询各学生都选了多少门课。难度：两颗星

```
select s.id,s.name,count(*) number from student s 
left join scores r on s.id = r.s_id
group by s.id,s.name;
```

28、查询课程名称为”java”，且分数低于60的学生姓名和分数。 难度：三颗星

```
select s.id,s.name,r.score from student s 
left join scores r on s.id = r.s_id
left join course c on r.c_id = c.id
where c.name = 'java' and r.score < 60;
```

29、查询学过”张楠”老师授课的同学的信息 。 难度：四颗星

```
select s.id,s.name from student s 
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id
left join teacher t on c.t_id = t.id 
where t.name = '张楠';
```

30、查询没学过“张楠”老师授课的同学的信息 。 难度：五颗星

```
select * from student where id not in
(select distinct s.id from student s 
left join scores r on r.s_id = s.id
left join course c on c.id = r.c_id
left join teacher t on c.t_id = t.id 
where t.name = '张楠');
```

