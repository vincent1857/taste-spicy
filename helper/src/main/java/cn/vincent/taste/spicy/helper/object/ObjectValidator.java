/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.vincent.taste.spicy.helper.object;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author vincent
 * @version 1.0 2020/1/9 17:20
 */
@Slf4j
public class ObjectValidator {

    private static final String MSG_PREFFIX = "{";
    private static final String MSG_SUFFIX = "}";

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = factory.getValidator();

    private ObjectValidator(){

    }

    public static <T> void valid(T t) {
//        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
//        if(constraintViolations != null && constraintViolations.size() > 0){
//            ConstraintViolation<T> constraintViolation = constraintViolations.iterator().next();
//            String message = constraintViolation.getMessage();
//            if(StringUtils.isNotBlank(message) && message.startsWith(MSG_PREFFIX) && message.endsWith(MSG_SUFFIX)){
//                message = message.substring(1, message.length() - 1);
//            }
//
//            String feild = constraintViolation.getPropertyPath().toString();
//            log.error(feild + " valid error");
//
//            Object[] args = constraintViolation.getExecutableParameters();
//            String[] params = null;
//            if (args != null && args.length > 0) {
//                params = new String[args.length];
//                for (int i = 0; i < args.length; i++){
//                    params[i] = args[i] != null ? String.valueOf(args[i]) : null;
//                }
//            }
//            ErrorCode error = new ErrorCode(message, message, params);

//            ErrorCode error = CommonErrorCodes.VALID_PARAMS_ERROR.toErrorCode();
//            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
//                String message = constraintViolation.getMessage();
//                if(StringUtils.isNotBlank(message) && message.startsWith("{") && message.endsWith("}")){
//                    message = message.substring(1, message.length() - 1);
//                }
//
//                ValidateMessage validator = new ValidateMessage(constraintViolation.getPropertyPath().toString(), message, message, constraintViolation.getExecutableParameters());
//                error.addValidator(validator);
//            }
//            throw new BusinessException(error);
//        }
    }
}
