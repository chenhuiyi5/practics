import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import netsky.cabp.org.SignerIntf;
import netsky.cabp.util.TypeUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.zip.ZipFiles;

public class HtmlToPDF implements Controller {

	private SignerIntf signer;
	private static final String FONT = "simhei.ttf";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//验证用户登录
		signer.checkSignedOn(request);		
		//判断多选还是单条记录(编写一个数据类型转换工具类，使用工具类做数据转换)
		int isSelect = TypeUtil.ObjectToInt(request.getParameter("isSelect"));
		if (isSelect==0) {			
			onePDF(request, response);
		} else {
			String downPath = manyPDF(request, response);
			if (downPath!=null) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				response.getWriter().write(downPath);
			}
		}
        return null;
	}
	
	/**
	 * 
	 * 创建PDF 
	 *
	 */
	public static void createPdf(String content,ServletOutputStream os,String imgBasePath) throws DocumentException, IOException,com.lowagie.text.DocumentException {
		//创建渲染器
        ITextRenderer renderer = new ITextRenderer();
        //创建字体解析器
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //添加中文字体
        fontResolver.addFont(FONT, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        //解析html生成pdf
        renderer.setDocumentFromString(content);
        //解决图片相对路径的问题
        renderer.getSharedContext().setBaseURL(imgBasePath);
        renderer.layout();
        try {
        	renderer.createPDF(os);
	        renderer.finishPDF();
		    os.flush();
		    os.close();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
	/**
	 * 
	 * 创建PDF 到本地
	 *
	 */
	public static void createPdf2(String content,OutputStream os,String imgBasePath) throws DocumentException, IOException,com.lowagie.text.DocumentException {
		//创建渲染器
        ITextRenderer renderer = new ITextRenderer();
        //创建字体解析器
        ITextFontResolver fontResolver = renderer.getFontResolver();
        //添加中文字体
        fontResolver.addFont(FONT, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
        //解析html生成pdf
        renderer.setDocumentFromString(content);
        //解决图片相对路径的问题
        renderer.getSharedContext().setBaseURL(imgBasePath);
        renderer.layout();
        renderer.createPDF(os);
        renderer.finishPDF();
        os.flush();
		os.close();
	}
	
	/**
	 * 勾选一个时,只创建一个PDF文件,以下载的形式
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void onePDF(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取request请求过来的参数
		int cvId = TypeUtil.ObjectToInt(request.getParameter("cvId"));
		int itemId = TypeUtil.ObjectToInt(request.getParameter("itemId"));
		String par_files = TypeUtil.ObjectToString(request.getParameter("par_files"));
		//获取工程部署的目录
	    String realPath = request.getSession().getServletContext().getRealPath("");
	    //图片在工程中的基础路径
	    String imgBasePath = new File(realPath+"/jsp/").toURI().toURL().toString(); 
		//请求的域名，如：http://localhost:8080/oseman
		String domainURL = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		//拼接需要提取页面内容的请求URL
		String url = domainURL + "......";
		//获取当前登录jssessionId，用于避免下面爬取页面时提示登录
		Cookie[] cookies = request.getCookies();
		String jsessionid = "";
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("JSESSIONID")) {
				jsessionid = cookie.getValue();
			}
		}		
		// 提取页面信息
		String[] pageInfo = extractPageInfo(url, jsessionid);						      
		//使用jsoup把html转为xhtml，补全结束标签，因为itext对html有着严格的要求，必须有开始和结束成对标签
	    org.jsoup.nodes.Document jdoc = Jsoup.parse(pageInfo[0]);        
		jdoc.outputSettings()
				.syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
				.escapeMode(Entities.EscapeMode.xhtml);
	    String content = jdoc.html();
	    String filename =pageInfo[1];
	    //文件名称
	    String fullname = new String(filename.getBytes(), "iso-8859-1") + ".pdf";
	    //getOutputStream() has already been called for this response
	    //response.reset();
	    response.setContentType("multipart/form-data");
		response.setHeader("Content-disposition", "attachment;filename=" + fullname);// 设置头信息
		ServletOutputStream os = response.getOutputStream(); 
	    
	    //创建PDF
	    ConvertPDF.createPdf(content,os,imgBasePath);
	}
	
	/**
	 * 勾选多个时,创建多个PDF在本地,无需弹出下载
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static String manyPDF(HttpServletRequest request,HttpServletResponse response) throws Exception {	
		String now=String.valueOf(new Date().getTime());
		//获取工程部署的目录
		String realPath = request.getSession().getServletContext().getRealPath("");
        //PDF在工程中的路径
        String PDFPath = realPath+"\\myTest\\"+now; 
        //PDF的ZIP在工程中的路径
        String ZIPPath = realPath+"\\myTest\\"+now; 
        //图片在工程中的基础路径
        String imgBasePath = new File(realPath+"/jsp/").toURI().toURL().toString(); 
        //请求的域名，如：http://localhost:8080/oseman
        String domainURL = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort() + request.getContextPath();
		//获取多选的ID
		String idsStr = TypeUtil.ObjectToString(request.getParameter("par_ids"));
		String[] idsArr = idsStr.split(",");		
		// 清空之前残留的文件
		delAllFile(PDFPath);
		delAllFile(ZIPPath);
		//逐个请求,生成文件
		for (int i = 0; i < idsArr.length; i++) {
			// 获取request请求过来的参数
			int cvId = TypeUtil.ObjectToInt(request.getParameter("cvId"));
			String par_files = TypeUtil.ObjectToString(request.getParameter("par_files"));
			//拼接需要提取页面内容的请求URL
			String url = domainURL + ".......";
			//获取当前登录jssessionId，用于避免下面爬取页面时提示登录
			Cookie[] cookies = request.getCookies();
			String jsessionid = "";
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					jsessionid = cookie.getValue();
				}
			}		
			// 提取页面信息
			String[] pageInfo = extractPageInfo(url, jsessionid);						      
			//使用jsoup把html转为xhtml，补全结束标签，因为itext对html有着严格的要求，必须有开始和结束成对标签
	        org.jsoup.nodes.Document jdoc = Jsoup.parse(pageInfo[0]);        
			jdoc.outputSettings()
					.syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
					.escapeMode(Entities.EscapeMode.xhtml);
			String filename = pageInfo[1];
	        String content = jdoc.html();
	        
	        //生成文件路径
	        File outDir =new File(PDFPath);
	        if (!outDir.exists()) {
	        	outDir.mkdirs();
			}
	        //文件名称
	        filename=filename==null?now+"_"+i:filename;
	        filename+=".pdf";
	        String file=PDFPath+"\\"+filename;
			OutputStream fileOutputStream = new FileOutputStream(file);
			//创建PDF
	        ConvertPDF.createPdf2(content,fileOutputStream,imgBasePath);
		}
		//生成文件路径
        File outDir =new File(ZIPPath);
        if (!outDir.exists()) {
        	outDir.mkdirs();
		}
		String zipName=String.valueOf(new Date().getTime());
		if(ZipFiles.fileToZip(PDFPath, ZIPPath, zipName)) {
			String zipDownPath=request.getContextPath()+"/myTest/"+now+"/"+zipName+".zip";
			return zipDownPath;
		};
		return null;
	}
	
	/**
	 * 
	 * 根据URL提取页面的信息
	 * 
	 * 
	 */
	public static String[] extractPageInfo(String url, String jsessionid)
			throws IOException {
		String[] pageInfo = new String[4];
		// 获取URL返回的页面
		org.jsoup.nodes.Document doc = Jsoup.connect(url)
				.cookie("JSESSIONID", jsessionid).get();
		// 提取class为warp的div部分
		org.jsoup.nodes.Element content = doc.select("div.warp").first();
		pageInfo[0] = content.html();
		//文件名
		pageInfo[1] = content.attr("data");
		return pageInfo;
	}

	public SignerIntf getSigner() {
		return signer;
	}

	public void setSigner(SignerIntf signer) {
		this.signer = signer;
	}
	
	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
