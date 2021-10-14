package com.sys.market.controller.api;

import com.sys.market.advice.exception.CConflictException;
import com.sys.market.entity.Chat;
import com.sys.market.dto.IdInfo;
import com.sys.market.dto.criteria.Criteria;
import com.sys.market.entity.Message;
import com.sys.market.entity.User;
import com.sys.market.dto.criteria.ChatCriteria;
import com.sys.market.dto.result.CommonResult;
import com.sys.market.dto.result.ListResult;
import com.sys.market.dto.result.PageResult;
import com.sys.market.dto.result.SingleResult;
import com.sys.market.service.ChatService;
import com.sys.market.service.MessageService;
import com.sys.market.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    private final ResponseService responseService;
    private final MessageService messageService;

    @Operation(summary = "지정한 사용자와의 1:1 채팅방 id 조회")
    @GetMapping("/{userId}/id")
    public SingleResult<IdInfo<String>> chat(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                             @PathVariable String userId,
                                             HttpServletResponse response){
        String chatId = chatService.findChatId(user.getId(), userId);
        if(chatId == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        return responseService.getSingleResult(new IdInfo<>(chatId));
    }

    @Operation(summary = "현재 사용자의 채팅방 리스트 조회")
    @GetMapping
    public PageResult<Chat> chatList(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                     @ModelAttribute Criteria criteria,
                                     HttpServletResponse response){
        ChatCriteria chatCriteria = new ChatCriteria();
        chatCriteria.setPageSize(criteria.getPageSize());
        chatCriteria.setPage(criteria.getPage());
        chatCriteria.setUserId(user.getId());

        List<Chat> chatList = new ArrayList<>();

        int totalChatCount = chatService.findChatCount(user.getId());
        if(totalChatCount <= (criteria.getPage()-1) * criteria.getPageSize()){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return responseService.getPageResult(chatList, chatCriteria);
        }

        criteria.paging(totalChatCount);
        chatList = chatService.findChatList(chatCriteria);
        return responseService.getPageResult(chatList, criteria);
    }

    @Operation(summary = "채팅방 등록")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResult chatAdd(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                @RequestBody @Valid ChatRequest chatRequest){
        if(chatService.findChatExist(user.getId(), chatRequest.getUserId()))
            throw new CConflictException("이미 해당 유저와의 채팅방이 존재합니다.");

        String chatId = chatService.createChatRoom(user.getId(), chatRequest.getUserId());
        return responseService.getSingleResult(new IdInfo<>(chatId));
    }

    @Operation(summary = "지정한 채팅방의 메시지 리스트 조회")
    @GetMapping("/{id}/messages")
    @PreAuthorize("isAuthenticated() and (@chatService.findChatExist(#id, principal.id))")
    public ListResult<Message> messageList(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                           @PathVariable String id,
                                           @RequestParam(required = false, defaultValue = "10") int limit){
        return responseService.getListResult(messageService.findMessageListByChatId(id, limit));
    }

    @Getter
    @Setter
    public static class ChatRequest{
        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private String userId;
    }
}
