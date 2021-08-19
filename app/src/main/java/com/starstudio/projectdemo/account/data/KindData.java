package com.starstudio.projectdemo.account.data;

import com.wheelpicker.widget.PickString;

import java.util.List;

/**
 *  添加账单（AccoAddFragment）中选择种类的级联滚轮数据类型
 */
public class KindData {
    public List<KindDetailFirst> kinds;

    public KindData(List<KindDetailFirst> kinds) {
        this.kinds = kinds;
    }

    public static class KindDetailFirst implements PickString{
        public String kindFrist;
        public List<KindDetailSecond> kindSeconds;

        public KindDetailFirst(String kindFrist, List<KindDetailSecond> kindSeconds) {
            this.kindFrist = kindFrist;
            this.kindSeconds = kindSeconds;
        }

        @Override
        public String pickDisplayName() {
            return kindFrist;
        }
    }

    public static class KindDetailSecond implements PickString{
        public String kindSecond;

        public KindDetailSecond(String kindSecond) {
            this.kindSecond = kindSecond;
        }

        @Override
        public String pickDisplayName() {
            return kindSecond;
        }
    }
}
