//定义代码生成后存放的位置
namespace java com.ipman.rpc.thrift.provide.springboot.pojo

//将shrift的数据类型格式转换为java习惯的格式
typedef i16 short
typedef i32 int
typedef i64 long
typedef string String
typedef bool boolean

//定义demo对象
struct DemoPOJO {
    1:optional String name
}

//定义数据异常
exception ExceptionPOJO {
    //optional 可选 非必传
    1:optional int code,
    2:optional String msg
}

//定义操作demo服务
service DemoServiceDTO {
    //根据名称返回一个demo，required 必传项
    DemoPOJO getStudentByName(1:required String name) throws (1:ExceptionPOJO dataException),

    //保存一个demo信息 无返回 抛出DataException异常
    void save(1:required DemoPOJO demo) throws (1:ExceptionPOJO dataException)
}
