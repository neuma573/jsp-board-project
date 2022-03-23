package com.lguplus.medialog.project.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.lguplus.medialog.project.common.utils.DownloadUtils;
import com.lguplus.medialog.project.common.utils.SpringUtils;



@Controller
@RequestMapping("/page/board")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BoardService svc;
	private File file;
	HttpSession session;
	boolean PagingByNew = false;
	Integer currPage;
	Integer displayRowCount = 10;
	
	//페이지에 표시할 게시글 수 세션에 넣기
	@RequestMapping(value="/setPageCnt")
	public String setPageCnt(@RequestParam(value = "displayRowCount") Integer displayRowCount) {
		this.displayRowCount =  displayRowCount;
		logger.info("게시글 표시 갯수 ::"+displayRowCount);
		
		return "redirect:/page/board";
	}
	
	//페이징 방식 결정
	@RequestMapping(value="/setPagingMethod")
	public String setPaging() {
		PagingByNew = !PagingByNew;
		logger.info("최신순으로 표시 ::"+PagingByNew);
		
		return "redirect:/page/board";
	}
	
	//게시판 리스트 출력
	@GetMapping("")
	public String boardList(PageVO pageVO, Model model) throws Exception{
		pageVO.setDisplayRowCount(displayRowCount);
		pageVO.pageCalculate(svc.selectBoardCount());
		List<?> listview;
		if (PagingByNew) {
			listview = svc.selectBoardListByNew(pageVO);
		} else {
			listview = svc.selectBoardList(pageVO);
		}
		model.addAttribute("list", listview);
		model.addAttribute("pageVO", pageVO);

		return "board/board.empty";
	}
	
    //게시판 게시글 디테일
	@GetMapping("boardView")
	public String openBoardDetail(@RequestParam Integer id, Model model) throws Exception {
		List<ReplyVO> list = svc.openCommentList(id);
		FileVO listview = svc.getFileList(id);
		model.addAttribute("listview",listview);
		model.addAttribute("list",list);
		svc.boardViewUpdate(id);
		BoardVO board = svc.getBoardDetail(id);
		model.addAttribute("board",board);
		
		return "board/view.empty";
	}
	

	//검색결과 출력
	@PostMapping("searchResult")
	public String boardSearchList(@RequestParam(value = "type") String type, @RequestParam(value = "searchKeyword") String searchKeyword, Model model){
		if(type.equals("title")) {
			List<BoardVO> listview = svc.searchBoardListByTitle(searchKeyword);
			logger.info("제목으로 검색, 키워드 ::"+searchKeyword);
			model.addAttribute("list", listview);
		}
		else {
			List<BoardVO> listview = svc.searchBoardListByContent(searchKeyword);
			logger.info("내용으로 검색, 키워드 ::"+searchKeyword);
			model.addAttribute("list", listview);
		}

        return "board/result.empty";
}
	
	//게시판 글쓰기
	@RequestMapping(value="/pageWrite", method = RequestMethod.POST)
	public String uploadBoard(BoardVO board, @RequestParam("uploadFile") MultipartFile uploadFile, FileVO fileVO) throws Exception {

		board.setBrdWriter(SpringUtils.getCurrentUser().getUserId());
		svc.uploadBoard(board);
		if(!uploadFile.isEmpty()) {
			logger.info("파일업로드 로그 : "+uploadFile);
			logger.info(uploadFile.getName());
			logger.info(Long.toString(uploadFile.getSize()));
			logger.info("파일명 : "+uploadFile.getOriginalFilename());
			uploadFile(fileVO, uploadFile, board);
		}
		return "redirect:/page/board";
	}	
	
	//파일업로드
	public void uploadFile(FileVO fileVO, MultipartFile uploadFile, BoardVO board) throws IOException{
        String originalFileExtension = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf("."));
        String storedFileName =SpringUtils.getCurrentUserName() + System.currentTimeMillis() + originalFileExtension;
        String filePath = "D://";
        file = new File(filePath+storedFileName);
		DownloadUtils.upload(uploadFile, file);
        //파일 저장완료
        SecureRandom num = new SecureRandom();
        fileVO.setFileRandomNo(num.nextLong());
        fileVO.setFileBrdNo(board.getBrdNo());
        fileVO.setFileRealName(uploadFile.getOriginalFilename());
        fileVO.setFileSize(uploadFile.getSize());
        fileVO.setFileName(storedFileName);
        svc.uploadFile(fileVO);
	}	

	//다운로드
	@RequestMapping(value="/fileDownload")
    public void fileDownload(@RequestParam long fileRandomNo, @RequestParam Integer brdNo, HttpServletRequest request,HttpServletResponse response) throws IOException {
        logger.info("다운로드할 게시글 번호 : "+ brdNo);
    	
    	String path = "D:\\"; 
    	FileVO fileVO = svc.getFileList(brdNo);
        
    	if(fileVO.getFileRandomNo()!=fileRandomNo) {
            logger.info(fileRandomNo+" :난수 검증 실패");    		
    		return ;
    	}
        logger.info("난수 검증 성공 : "+ fileRandomNo);
        
        String filename = fileVO.getFileName();
        
        String realPath = path + filename;
        logger.info("파일 다운 경로 "+ realPath);

        file = new File(realPath);
        
        // 파일명 지정
        DownloadUtils.setDownloadHeader(fileVO.getFileRealName(), request, response);
        DownloadUtils.download(file, request, response);

    }
	



	//게시판 글작성 폼 호출
	@RequestMapping("/page")
	public String page() {
		return "board/somePage.empty";
	}


	
	//게시판 답글작성 폼 호출
	@RequestMapping("/boardReply")
	public String board(@RequestParam("id") Integer id, @RequestParam("origin") Integer origin, Model model) {
        BoardVO board = svc.getBoardDetail(id);
        model.addAttribute("board", board);    
		List<?> listview = svc.selectBoard6FileList(id);
		model.addAttribute("listview",listview);
    	model.addAttribute("id", id);
    	model.addAttribute("origin", origin);
		
		return "board/boardReply.empty";
	}
	

	//파일삭제
	@RequestMapping(value="fileDelete")
	public String deleteFile(@RequestParam("id") String id,@RequestParam("bid") Integer bid) throws IOException{
		svc.deleteFile(id);
		
		return "redirect:/page/board/boardModify?id="+bid;
	}
	
	//게시판 수정폼 호출
    @RequestMapping(value="boardModify")
    public String boardModify(@RequestParam("id") Integer id, Model model) throws Exception {
        BoardVO board = svc.getBoardDetail(id);
        model.addAttribute("board", board);    
		FileVO file = svc.getFileList(id);
		model.addAttribute("file",file);
        
        return "board/boardModify.empty";
        
    }
    
    // 게시판 글 수정
    @RequestMapping(value="/boardModifyRegist", method = RequestMethod.POST)
    public String boardModifyRegist(@RequestParam("uploadFile") MultipartFile file, FileVO fileVO, BoardVO board) throws Exception {
		String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.Sec"));
		board.setBrdModDt(formatDate);
		svc.boardModifyRegist(board);
		if(!file.isEmpty()) {
			logger.info("파일업로드 로그"+file);
			logger.info(file.getName());
			logger.info(null, file.getSize());
			System.out.println(file.getOriginalFilename());
			uploadFile(fileVO, file, board);
		}	
		return "redirect:/page/board";
    }
    
    //게시판 글삭제
    @RequestMapping(value="/boardDelete")
    public String boardDelete(@RequestParam("id") Integer id) throws Exception {
        if (!svc.boardDelete(id)) {
            return "board/BoardFailure";
        }
		svc.deleteFileByParents(id);
        svc.commentDeleteByParents(id);
        return "redirect:/page/board";
    }
    
    //댓글저장
    @RequestMapping(value = "/commentPost")
    public String boardReplySave(ReplyVO boardReplyInfo) {
        boardReplyInfo.setReWriter(SpringUtils.getCurrentUser().getUserId());
        svc.insertBoardReply(boardReplyInfo);
        svc.addCommentCnt(boardReplyInfo.getReBrdNo());

        return "redirect:/page/board/boardView?id=" + boardReplyInfo.getReBrdNo();
    }
    
    
    //댓삭제
    @RequestMapping(value = "/commentDelete")
    public String boardReplyDelete(@RequestParam("id") String id, @RequestParam("bid") String bid, ReplyVO boardReplyInfo) {
        
        if (!svc.deleteBoard6Reply(id)) {
            return "board/BoardFailure";
        }
        svc.subCommentCnt(bid);
        return "redirect:/page/board/boardView?id=" + bid;
    }
    
    
    
//    대댓입력창 호출
    @RequestMapping(value="/commentReply")
    public String commentReply(@RequestParam("id") int id, @RequestParam("bid") Integer bid, Model model) throws Exception {
    	model.addAttribute("id", id);
    	model.addAttribute("bid", bid);
    	return "board/commentReply.empty";
    }



	
    





}