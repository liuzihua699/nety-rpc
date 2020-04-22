package com.zihua.rpc.exception;

/**
 * @author by 刘子华.
 * create on 2020/04/22.
 * describe:
 */
public class NotFoundInstanceException extends RuntimeException{ 

    private static final long serialVersionUID = 8712340739785009618L;

    public NotFoundInstanceException() {
        super();
    }

    public NotFoundInstanceException(String message) {
        super(message);
    }
}
