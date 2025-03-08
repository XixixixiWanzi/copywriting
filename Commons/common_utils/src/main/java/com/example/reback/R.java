package com.example.reback;

public interface R {
    FailReBack FAIL = new FailReBack();
    SuccessReBack SUCCESS = new SuccessReBack();
    class FailReBack extends Reback {
        FailReBack(){
            super();
            super.result(ResultCode.FAIL);
        }

        @Override
        public Reback message(String message) {
            return super.message(message);
        }

        @Override
        public Reback data(String key, Object value) {
            return super.data(key, value);
        }


        public Reback setCode(ResultCode code){
            super.result(code);
            return this;
        }
    }
    class SuccessReBack extends Reback {
        SuccessReBack(){
            super();
            super.result(ResultCode.SUCCESS);
        }
        @Override
        public Reback message(String message) {
            return super.message(message);
        }

        @Override
        public Reback data(String key, Object value) {
            return super.data(key, value);
        }
    }
}
