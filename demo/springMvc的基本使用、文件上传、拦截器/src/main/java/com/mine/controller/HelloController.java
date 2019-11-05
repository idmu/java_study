package com.mine.controller;

import com.mine.domain.User;
import com.mine.exception.SysException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/sayHello")
    public String sayHello(Model model) {
        System.out.println("访问了hello方法");
        User user = new User();
        user.setUsername("李白");
        user.setPassword("234");
        user.setAge(15);
        model.addAttribute("user",user);
        return "success";
    }

    @RequestMapping("/getUser")
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("getuser方法执行了");
        // 手动转发需要完整路径
        // request.getRequestDispatcher("/WEB-INF/pages/success.jsp").forward(request,response);
        // 重定向
        //  response.sendRedirect(request.getContextPath() + "/index.jsp");
        // 直接响应数据
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("你好");
        return;
    }

    //传统的上传方式
    @RequestMapping("/upload")
    public String uploadFile(HttpServletRequest request) throws Exception {
        System.out.println("文件上传..");
        // 获取到需要上传文件的路径
        String realPath = request.getSession().getServletContext().getRealPath("/uploads/");
        // 获取file路径,向路径上传文件
        File file = new File(realPath);
        // 判断路径是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        // 创建磁盘文件工厂方法
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        // 解析request对象
        List<FileItem> list = fileUpload.parseRequest(request);
        // 遍历
        for (FileItem item : list) {
            // 判断是普通字段还是文件上传
            if (item.isFormField()) {
            } else {
                // 获取到上传文件的名称
                String fileName = item.getName();
                String uuid = UUID.randomUUID().toString().replace("-","");
                fileName = uuid + "_" + fileName;
                // 上传文件
                item.write(new File(file, fileName));
                // 删除临时文件
                item.delete();
            }
        }
        return "success";
    }

    @RequestMapping("/uploadFileBySpring")
    // 要求MultipartFile对象变量名称必须和表单file标签的name属性名称相同。
    public String uploadFileBySpring(HttpServletRequest request, MultipartFile upload) throws Exception {
        System.out.println("springmvc的文件上传");
        String path = request.getSession().getServletContext().getRealPath("/mvcUpload");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 通过upload获取文件的名字
        String filename = upload.getOriginalFilename();
        // 设置文件名为唯一值,拼接uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + "_" + filename;
        upload.transferTo(new File(path,filename));
        return "success";
    }

    @RequestMapping("/uploadOtherServlet")
    public String uploadOtherServlet(HttpServletRequest request, MultipartFile upload) throws Exception {
        // 获取上传的文件的路径
        String path = "http://localhost:8080/uploads/";
        String filename = upload.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + "_" + filename;
        // 创建客户端的连接
        Client client = Client.create();
        // 和图片服务器进行连接
        WebResource webResource = client.resource(path + filename);
        // 上传
        webResource.put(upload.getBytes());
        return "success";
    }
     // 异常处理类
    @RequestMapping("/exceptionTest")
    public String exceptionTest() throws SysException {
        System.out.println("异常处理测试");
        try {
            int i = 110 / 0;
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            throw new SysException("出现异常了,自定义异常友好界面抛出");
        }
        return "success";
    }

    // 拦截器方法
    @RequestMapping("testInterceptor")
    public String testInterceptor() {
        System.out.println("拦截器方法");
        return "success";
    }
}
