package com.lguplus.medialog.project.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/page/board")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BoardService svc;
	private File file;


	
	//게시판 리스트 출력
	@GetMapping("")
	public String boardList(PageVO pageVO, Model model) throws Exception {

        pageVO.pageCalculate(svc.selectBoardCount() ); // startRow, endRow

        List<?> listview   = svc.selectBoardList(pageVO);
       
        model.addAttribute("list", listview);
        model.addAttribute("pageVO", pageVO);

        return "board/board.empty";
}
	
	//검색결과 출력
	@PostMapping("result")
	public String boardSearchList(@RequestParam String searchKeyword, ModelMap modelMap) throws Exception {
        List<BoardVO> listview = svc.searchBoardList(searchKeyword);
        modelMap.addAttribute("list", listview);
        return "board/result.empty";
}	
	//다운로드
    @RequestMapping(value = "/fileDownload")
    public void fileDownload(HttpServletRequest request,HttpServletResponse response) {
        String path = "D:\\"; 
        
        String filename = request.getParameter("filename");
        String downname = request.getParameter("downname");
        String realPath = "";
        
        if (filename == null || "".equals(filename)) {
            filename = downname;
        }
        
        try {
            filename = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println("UnsupportedEncodingException");
        }
        
        realPath = path + filename;
        System.out.println(realPath);
        File file1 = new File(realPath);
        if (!file1.exists()) {
            return ;
        }
        
        // 파일명 지정
        response.setHeader("Content-Disposition", "attachment; filename=\"" + downname + "\"");
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(realPath);

            int ncount = 0;
            byte[] bytes = new byte[512];

            while ((ncount = fis.read(bytes)) != -1 ) {
                os.write(bytes, 0, ncount);
            }
            fis.close();
            os.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }
	
    //게시판 게시글 디테일
	@GetMapping("view")
	public String openBoardDetail(@RequestParam int id, Model model) throws Exception {
		List<ReplyVO> list = svc.openCommentList(id);
		List<?> listview = svc.selectBoard6FileList(id);
		model.addAttribute("listview",listview);
		model.addAttribute("list",list);
		svc.boardViewUpdate(id);
		BoardVO board = svc.getBoardDetail(id);
		model.addAttribute("board",board);
		

		
		return "board/view.empty";
	}


	//게시판 글작성 폼 호출
	@RequestMapping("/page")
	public String page() {
		return "board/somePage.empty";
	}

	//게시판 글쓰기
	@RequestMapping(value="/pageWrite", method = RequestMethod.POST)
	public String uploadBoard(BoardVO board, @RequestParam("uploadFile") MultipartFile file, FileVO fileVO) throws Exception {

	
		board.setBrdWriter("foo");
		svc.uploadBoard(board);
		if(!file.isEmpty()) {
			System.out.println("파일업로드 로그"+file);
			System.out.println(file.getName());
			System.out.println(file.getSize());
			System.out.println(file.getOriginalFilename());
			uploadFile(fileVO, file, board);
		}
		return "redirect:/page/board";
	}
	
	//파일업로드
	public void uploadFile(FileVO fileVO, MultipartFile uploadFile, BoardVO board) throws IOException{
		//파일명
        String originalFile = uploadFile.getOriginalFilename();
        //파일명 중 확장자만 추출                                                //lastIndexOf(".") - 뒤에 있는 . 의 index번호
        String originalFileExtension = originalFile.substring(originalFile.lastIndexOf("."));
        //fileuploadtest.doc
        //lastIndexOf(".") = 14(index는 0번부터)
        //substring(14) = .doc
        

        String storedFileName = originalFile+System.currentTimeMillis() + originalFileExtension;
        String filePath = "D://";
        file = new File(filePath + storedFileName);
        //파일 저장
        uploadFile.transferTo(file);
        fileVO.setFileBrdNo(board.getBrdNo());
        fileVO.setFileRealName(originalFile);
        fileVO.setFileSize(uploadFile.getSize());
        fileVO.setFileName(storedFileName);
        svc.uploadFile(fileVO);
	}
	
	//게시판 수정폼 호출
    @RequestMapping(value="boardModify")
    public String boardModify(@RequestParam("id") int id, Model model) throws Exception {
        BoardVO board = svc.getBoardDetail(id);
        model.addAttribute("board", board);    
		List<?> listview = svc.selectBoard6FileList(id);
		model.addAttribute("listview",listview);
        
        return "board/boardModify.empty";
        
    }
    
    // 게시판 글 수정
    @RequestMapping(value="boardModifyRegist")
    public String boardModifyRegist(BoardVO board) throws Exception {
		String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.Sec"));
		board.setBrdModDt(formatDate);
		svc.boardModifyRegist(board);
			
		return "redirect:/page/board";
    }
    
    //게시판 글삭제
    @RequestMapping(value="/boardDelete")
    public String boardDelete(@RequestParam("id") int id) throws Exception {
        
        svc.boardDelete(id);
        return "redirect:/page/board";
    }
    
    //댓글저장
    @RequestMapping(value = "/commentPost")
    public String board6ReplySave(ReplyVO boardReplyInfo) {
        boardReplyInfo.setReWriter("foo");
        svc.insertBoardReply(boardReplyInfo);
        svc.addCommentCnt(boardReplyInfo.getReBrdNo());

        return "redirect:/page/board/view?id=" + boardReplyInfo.getReBrdNo();
    }
    
    
    //댓삭제
    @RequestMapping(value = "/commentDelete")
    public String board6ReplyDelete(@RequestParam("id") String id, @RequestParam("bid") String bid, ReplyVO boardReplyInfo) {
        
        if (!svc.deleteBoard6Reply(id)) {
            return "board/BoardFailure";
        }
        svc.subCommentCnt(bid);
        return "redirect:/page/board/view?id=" + bid;
    }
    
    
    
//    대댓입력창 호출
    @RequestMapping(value="/commentReply")
    public String commentReply(@RequestParam("id") int id, @RequestParam("bid") int bid, Model model) throws Exception {
    	model.addAttribute("id", id);
    	model.addAttribute("bid", bid);
    	return "board/commentReply.empty";
    }



	
    





}