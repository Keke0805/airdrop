package com.swufe.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class FileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 判断上传的文件普通表单还是带文件的表单
        if (!ServletFileUpload.isMultipartContent(request)) {
            return;//终止方法运行,说明这是一个普通的表单,直接返回
        }
        //创建上传文件的保存路径,建议在WEB-INF路径下,安全,用户无法直接访间上传的文件;
        String uploadPath =this.getServletContext().getRealPath("/WEB-INF/upload");
        File uploadFile = new File(uploadPath);
        if (!uploadFile.exists()){
            uploadFile.mkdir(); //创建这个目录
        }
        //缓存，临时文件
        //临时路径，假如文件超过了预期的大小，我们就把他放在一个临时文件中，过几天自动删除，或者提醒用户转为永久
        String tmpPath = this.getServletContext().getRealPath("/WEB-INF/tmp");
        File tmpFile = new File(tmpPath);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();//创建这个临时目录
        }

        // 使用 Apache的文件上传组件来实现, common-fileupload;
        try {
            // 1、创建DiskFileItemFactory对象，处理文件路径或者大小限制
            DiskFileItemFactory factory = getDiskFileItemFactory(tmpFile);

            // 2、获取ServletFileUpload
            ServletFileUpload upload = getServletFileUpload(factory);

            // 3、处理上传文件
            // 把前端请求解析，封装成FileItem对象，需要从ServletFileUpload对象中获取
            String msg = uploadParseRequest(upload, request, uploadPath);

            // Servlet请求转发消息
            System.out.println(msg);
            if(msg.equals("文件上传成功!")) {
                // Servlet请求转发消息
                request.setAttribute("msg",msg);
                request.getRequestDispatcher("info.jsp").forward(request, response);
            }else {
                msg ="请上传文件";
                request.setAttribute("msg",msg);
                //转发
                request.getRequestDispatcher("info.jsp").forward(request, response);
            }

        } catch (FileUploadException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }
    // 1、创建DiskFileItemFactory对象，处理文件路径或者大小限制
    /*
     * 通过这个工厂设置一个缓冲区,当上传的文件大于这个缓冲区的时候,
     * 将他放到临时文件 factory.setSizeThreshold(1024 *1024);
     * 缓存区大小为1M factory.setRepository (file);//临时目录的保存目录,需要一个File
     */
    public static DiskFileItemFactory getDiskFileItemFactory(File file) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 通过这个工厂设置一个缓冲区,当上传的文件大于这个缓冲区的时候,将他放到临时文件中;
        factory.setSizeThreshold(1024 * 1024);// 缓冲区大小为1M
        factory.setRepository(file);// 临时目录的保存目录,需要一个file
        return factory;
    }
    // 2、获取ServletFileUpload
    public static ServletFileUpload getServletFileUpload(DiskFileItemFactory factory) {
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 监听文件上传进度
        upload.setProgressListener(new ProgressListener() {

            // pBYtesRead:已读取到的文件大小
            // pContextLength:文件大小
            public void update(long pBytesRead, long pContentLength, int pItems) {
                System.out.println("总大小：" + pContentLength + "已上传：" + pBytesRead);
            }
        });

        // 处理乱码问题
        upload.setHeaderEncoding("UTF-8");
        // 设置单个文件的最大值
        upload.setFileSizeMax(1024 * 1024 * 10);
        // 设置总共能够上传文件的大小
        // 1024 = 1kb * 1024 = 1M * 10 = 10м
        upload.setSizeMax(1024*1024*10);

        return upload;
    }
    // 3、处理上传文件
    //把前端请求解析，封装成FileItem对象，需要从ServletFileUpload对象中获取
    public static String uploadParseRequest(ServletFileUpload upload, HttpServletRequest request, String uploadPath)
            throws FileUploadException, IOException {

        String msg = "";

        // 把前端请求解析，封装成FileItem对象
        List<FileItem> fileItems = upload.parseRequest(request);
        for (FileItem fileItem : fileItems) {
            //fileItem每一个表单对象
            if (fileItem.isFormField()) {// 判断上传的文件是普通的表单还是带文件的表单
                // getFieldName指的是前端表单控件的name;
                String name = fileItem.getFieldName();
                String value = fileItem.getString("UTF-8"); // 处理乱码
                System.out.println(name + ": " + value);
            } else {// 判断它是上传的文件

                // ============处理文件==============

                // 拿到文件名
                String uploadFileName = fileItem.getName();
                System.out.println("上传的文件名: " + uploadFileName);
                if (uploadFileName.trim().equals("") || uploadFileName == null) {
                    continue;
                }

                // 获得上传的文件名/images/girl/paojie.png
                String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("/") + 1);
                // 获得文件的后缀名
                String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);

                /*
                 * 如果文件后缀名fileExtName不是我们所需要的 就直按return.不处理,告诉用户文件类型不对。
                 */

                System.out.println("文件信息[件名: " + fileName + " ---文件类型" + fileExtName + "]");
                // 可以使用UID（唯一识别的通用码),保证文件名唯一
                // 0UID. randomUUID(),随机生一个唯一识别的通用码;
                String uuidPath = UUID.randomUUID().toString();

                // ================处理文件完毕==============

                // 存到哪? uploadPath
                // 文件真实存在的路径realPath
                String realPath = uploadPath + "/" + uuidPath;
                // 给每个文件创建一个对应的文件夹
                File realPathFile = new File(realPath);
                if (!realPathFile.exists()) {
                    realPathFile.mkdir();
                }
                // ==============存放地址完毕==============


                // 获得文件上传的流
                InputStream inputStream = fileItem.getInputStream();
                // 创建一个文件输出流
                // realPath =真实的文件夹;
                // 差了一个文件;加上翰出文件的名产"/"+uuidFileName
                FileOutputStream fos = new FileOutputStream(realPath + "/" + fileName);
                System.out.println("path:"+realPath + "/" + fileName);
                // 创建一个缓冲区
                byte[] buffer = new byte[1024 * 1024];
                // 判断是否读取完毕
                int len = 0;
                // 如果大于0说明还存在数据;
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                // 关闭流
                fos.close();
                inputStream.close();

                msg = "文件上传成功!";
                fileItem.delete(); // 上传成功,清除临时文件
                //=============文件传输完成=============
            }
        }
        return msg;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
