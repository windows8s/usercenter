package com.gdgd.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdgd.usercenter.common.BaseResponse;
import com.gdgd.usercenter.common.ErrorCode;
import com.gdgd.usercenter.common.ResultUtils;
import com.gdgd.usercenter.excepiton.BusinessException;
import com.gdgd.usercenter.model.domain.User;
import com.gdgd.usercenter.model.domain.request.UserLoginRequest;
import com.gdgd.usercenter.model.domain.request.UserRegisterRequest;
import com.gdgd.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gdgd.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.gdgd.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author gdgd
 */

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null) {
            /*return ResultUtils.error(ErrorCode.PARAMS_ERROR);*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long userRegister = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);

        return ResultUtils.success(userRegister);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)) {
            return null;
        }
        User userLogin = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userLogin);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null) {
            return null;
        }

        int userLogout = userService.userLogout(request);

        return ResultUtils.success(userLogout);



    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        //获取用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }

        Long userId = currentUser.getId();
        // todo 校验用户是否合法（例如封号）
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

        return ResultUtils.success(list);



    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //逻辑删除
        boolean removeById = userService.removeById(id);
        return ResultUtils.success(removeById);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User)userObj;
        if(user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }

}
