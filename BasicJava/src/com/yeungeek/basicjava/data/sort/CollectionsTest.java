package com.yeungeek.basicjava.data.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionsTest {

    public static void main(String[] args) {
        List<VideoInfo> videoInfos = new ArrayList<>();
        /**
         * 1574826450000
         * 1574826505000
         * 1574826699000
         * 1574826723000
         * 1574827299000
         * 1574675238000
         * 1637833918000
         * 1637846110000
         */

        videoInfos.add(new VideoInfo().setIndex(0).setCreateTime(1574826450000L));
        videoInfos.add(new VideoInfo().setIndex(1).setCreateTime(1574826505000L));
        videoInfos.add(new VideoInfo().setIndex(2).setCreateTime(1574826699000L));
        videoInfos.add(new VideoInfo().setIndex(3).setCreateTime(1574826723000L));
        videoInfos.add(new VideoInfo().setIndex(4).setCreateTime(1574827299000L));
        videoInfos.add(new VideoInfo().setIndex(5).setCreateTime(1574675238000L));
        videoInfos.add(new VideoInfo().setIndex(6).setCreateTime(1637833918000L));
        videoInfos.add(new VideoInfo().setIndex(7).setCreateTime(1637846110000L));

        printlnList(videoInfos);

        Collections.sort(videoInfos, new Comparator<VideoInfo>() {
            @Override
            public int compare(VideoInfo o1, VideoInfo o2) {
                return (o2.createTime < o1.createTime) ? -1 : ((o2.createTime == o1.createTime) ? 0 : 1);
            }
        });
        System.out.println("##### sort ######");
        printlnList(videoInfos);
    }

    public static void printlnList(final List<VideoInfo> list) {
        for (VideoInfo videoInfo : list) {
            System.out.println("index: " + videoInfo.getIndex() + ", " + videoInfo.getCreateTime());
        }
    }

    public static class VideoInfo {
        private int index;
        private long createTime;

        public int getIndex() {
            return index;
        }

        public VideoInfo setIndex(int index) {
            this.index = index;
            return this;
        }

        public long getCreateTime() {
            return createTime;
        }

        public VideoInfo setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VideoInfo videoInfo = (VideoInfo) o;

            return createTime == videoInfo.createTime;
        }

        @Override
        public int hashCode() {
            return (int) (createTime ^ (createTime >>> 32));
        }

//        @Override
//        public int compareTo(VideoInfo o) {
//            return (int) (o.createTime - this.createTime);
//        }
    }
}
