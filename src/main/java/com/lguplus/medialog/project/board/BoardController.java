package com.lguplus.medialog.project.board;

import java.io.File;

import java.io.IOException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lguplus.medialog.project.common.utils.DownloadUtils;
import com.lguplus.medialog.project.common.utils.SpringUtils;

import lombok.Data;



@Controller
@Data
@ConfigurationProperties("app.brdcont")
@RequestMapping("/page/board")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private boolean viewCntByCookie;
	
	@Autowired
	private BoardService svc;
	private File file;
	HttpSession session;
	boolean PagingByNew = false;
	Integer currPage =1;
	Integer displayRowCount = 10;
	PageVO pageVO;
	String userName = SpringUtils.getCurrentUserName();
	BoardVO board;
	boolean selectOrigin = true;
	boolean selectRe = true;
	
	
	/* 페이지에 표시할 게시글 수 컨트롤러에 넣기 */
	@RequestMapping(value="/setPageCnt")
	public String setPageCnt(@RequestParam(value = "displayRowCount") Integer displayRowCount) {
		this.displayRowCount =  displayRowCount;
		logger.info("게시글 표시 갯수 ::"+displayRowCount);
		
		return "redirect:/page/board";
	}
		
	/* 페이징 방식 결정 */
	@RequestMapping(value="/setPagingMethod")
	public String setPaging() {
		PagingByNew = !PagingByNew;
		logger.info("최신순으로 표시 ::"+PagingByNew);
	
		return "redirect:/page/board";
	}
	
		/* 게시글 그룹 표시 */
	@RequestMapping(value="/selectOrigin")
	public String selectOrigin() {
		selectOrigin = !selectOrigin;
		logger.info("게시글 그룹 표시 ::"+selectOrigin);
		
		return "redirect:/page/board";
	}
		/* 답글 그룹 표시 */
	@RequestMapping(value="/selectRe")
	public String selectRe() {
		selectRe = !selectRe;
		logger.info("답글 그룹 표시 ::"+selectRe);
		
		return "redirect:/page/board";
	}
	
	
	
	
	/* 게시판 리스트 출력 */
	@GetMapping("")
	public String boardList(PageVO pageVO, Model model) throws Exception{
		logger.info("보드정보: "+ board);
		logger.info("오리진 표시 : "+ selectOrigin + " 답글 표시 :" + selectRe);
		userName = SpringUtils.getCurrentUserName();
		pageVO.setSelectOrigin(selectOrigin);
		pageVO.setSelectRe(selectRe);
		pageVO.setDisplayRowCount(displayRowCount);
		pageVO.setPagingByNew(PagingByNew);
		pageVO.pageCalculate(svc.selectBoardCount(pageVO));
		List<?> listview;
		if (PagingByNew) {
			listview = svc.selectBoardListByNew(pageVO);
		} else {
			listview = svc.selectBoardList(pageVO);
		}
		model.addAttribute("list", listview);
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("User", userName);
		this.currPage=pageVO.getPage();
		logger.info("현재 페이지 :: "+currPage);
		this.pageVO = pageVO;
		logger.info("pageVO 정보 :: "+pageVO);
		return "board/board.empty";
	}
	
	/* 게시판 게시글 디테일 */
	@GetMapping("boardView")
	public String openBoardDetail(@RequestParam Integer id, Model model,  HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ReplyVO> list = svc.openCommentList(id);
		FileVO listview = svc.getFileList(id);
		model.addAttribute("listview",listview);
		model.addAttribute("list",list);

		if(viewCntByCookie) {
			viewCountUp(id, request, response);
		}
		else {
			svc.boardViewUpdate(id);
		}
		BoardVO board = svc.getBoardDetail(id);
		model.addAttribute("board",board);
		this.board = board;
		logger.info("BoardVO정보: "+ board);
		return "board/view.empty";
		
	}
	private void viewCountUp(Integer id, HttpServletRequest request, HttpServletResponse response) {

	    Cookie oldCookie = null;
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals("postView")) {
	                oldCookie = cookie;
	            }
	        }
	    }

	    if (oldCookie != null) {
	        if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
	    		svc.boardViewUpdate(id);
	            oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
	            oldCookie.setPath("/");
	            oldCookie.setMaxAge(60 * 60 * 24);
	            response.addCookie(oldCookie);
	        }
	    } else {
    		svc.boardViewUpdate(id);
	        Cookie newCookie = new Cookie("postView","[" + id + "]");
	        newCookie.setPath("/");
	        newCookie.setMaxAge(60 * 60 * 24);
	        response.addCookie(newCookie);
	    }
	}
	
	
	
	
	

	/* 검색결과 출력 */
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

	/* 게시판 글작성 폼 호출 */
	@RequestMapping(value="/boardWrite")
	public String page(@RequestParam(required = false)String action, @RequestParam(required = false) Integer id, @RequestParam(required = false) Integer origin, Model model) {
		if(action==null) {
			model.addAttribute("title","게시글 작성");
			return "board/somePage.empty";
		}
		
        BoardVO board = svc.getBoardDetail(id);
		
		switch(action) {
		case "modify" :
			if(!userName.equals(board.getBrdWriter())) {
				model.addAttribute("err", "[다른 사람의 글은 수정할 수 없습니다]");
	            return "board/BoardFailure";
			}
			model.addAttribute("title",id+"번 게시글 수정");
			model.addAttribute("action","modify");

			FileVO file = svc.getFileList(id);
			model.addAttribute("file",file);
			

			break;
			
		case "reply" :
			model.addAttribute("title",id+"번 게시글에 답글 작성");
			model.addAttribute("action","reply"); 
			

	    	model.addAttribute("id", id);
	    	model.addAttribute("origin", origin);
			break;
		}
        model.addAttribute("board", board);
		
		
		
		return "board/somePage.empty";
	}
	
	/* 게시판 글쓰기 */
	@RequestMapping(value="/boardWriteRegist", method = RequestMethod.POST)
	public String uploadBoard(BoardVO board, @RequestParam(required = false) MultipartFile uploadFile,  FileVO fileVO, @RequestParam(required = false)String action,  @RequestParam(required = false) Integer brdDepth, @RequestParam(required = false) Integer brdNo) throws Exception {
		board.setBrdWriter(SpringUtils.getCurrentUser().getUserId());
		String formatDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		switch(action) {
		case "write" :
			svc.uploadBoard(board);
			break;
		case "reply" :
			svc.uploadBoard(board);
			break;
		case "modify" :
			board.setBrdNo(brdNo);
			board.setBrdModDt(formatDate);
			svc.boardModifyRegist(board);
			break;
		}
		
		
		
		
		
		

		
		
		if(!uploadFile.isEmpty()) {
			logger.info("파일업로드 로그 : "+uploadFile);
			logger.info(uploadFile.getName());
			logger.info(Long.toString(uploadFile.getSize()));
			logger.info("파일명 : "+uploadFile.getOriginalFilename());
			uploadFile(fileVO, uploadFile, board);
		}
		return "redirect:/page/board";
	}
    // 게시판 글 수정 등록

	
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
	
	
	

	//파일삭제
	@RequestMapping(value="fileDelete")
	public String deleteFile(@RequestParam("id") String id,@RequestParam("bid") Integer bid, Model model) throws IOException{
		if(id.equals("")) {

		}
		else {
		svc.deleteFile(id);
		}
		BoardVO board = svc.getBoardDetail(bid);
		board.setBrdNo(bid);
		
				if(!userName.equals(board.getBrdWriter())) {
					model.addAttribute("err", "[다른 사람의 글은 수정할 수 없습니다]");
		            return "board/BoardFailure";
				}
				model.addAttribute("title",id+"번 게시글 수정");
				model.addAttribute("action","modify");
				model.addAttribute("board", board);
		return "board/somePage.empty";
	}
	
	//게시판 수정폼 호출

    

    
    //게시판 글삭제
    @RequestMapping(value="/boardDelete")
    public String boardDelete(@RequestParam("id") Integer id, Model model) throws Exception {
        if(!userName.equals(board.getBrdWriter())) {
			model.addAttribute("err", "[다른 사람의 글은 삭제할 수 없습니다]");
            return "board/BoardFailure";
		}
    	else if (!svc.boardDelete(id)) {
			model.addAttribute("err", "답글이 존재하여 삭제할 수 없습니다");
            return "board/BoardFailure";
        }
		svc.deleteFileByParents(id);
        svc.commentDeleteByParents(id);
        
        if(pageVO.getTotRow()%10==1) {
        	currPage--;
        }
    
        
        
        return "redirect:/page/board?page="+currPage;
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
    public String boardReplyDelete(@RequestParam("id") String id, @RequestParam("bid") String bid, ReplyVO boardReplyInfo, Model model) {
    	

        if(!userName.equals(boardReplyInfo.getReWriter())) {
			model.addAttribute("err", "[다른 사람의 댓글은 삭제할 수 없습니다]");
            return "board/BoardFailure";
		}
    	else if (!svc.deleteBoard6Reply(id)) {
			model.addAttribute("err", "대댓글이 존재하여 삭제할 수 없습니다");
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