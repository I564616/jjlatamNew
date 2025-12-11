/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.jwt.service.exception;

public interface IErrorCode {
    public abstract String getCode();
    
    public abstract String getMessage();
}
