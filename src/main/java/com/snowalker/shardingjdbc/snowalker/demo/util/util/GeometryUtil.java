package com.snowalker.shardingjdbc.snowalker.demo.util.util;

/***
 * 经纬度计算公式
 */
public class GeometryUtil {

    private static final double DEF_PI = 3.14159265359; // PI
    private static final double DEF_2PI = 6.28318530712; // 2*PI
    private static final double DEF_PI180 = 0.01745329252; // PI/180.0
    private static final double DEF_R = 6370693.5; // radius of earth

    /***
     * 两点之间的直线距离
     * @param p1 起始地[经度,纬度]
     * @param p2 目的地[经度,纬度]
     * @return 距离(米)
     */
    public static Double point2point(double[] p1, double[] p2) {

        //参数验证
        if (p1 == null || p2 == null) {
            return null;
        }
        if (p1.length < 2 || p2.length < 2) {
            return null;
        }

        // 角度转换为弧度
        double ew1 = p1[0] * DEF_PI180;
        double ns1 = p1[1] * DEF_PI180;
        double ew2 = p2[0] * DEF_PI180;
        double ns2 = p2[1] * DEF_PI180;

        // 经度差
        double dew = ew1 - ew2;

        // 若跨东经和西经180度，进行调整
        if (dew > DEF_PI) {
            dew = DEF_2PI - dew;
        } else if (dew < -DEF_PI) {
            dew = DEF_2PI + dew;
        }
        double dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        double dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)

        // 勾股定理求斜边长
        return Math.sqrt(dx * dx + dy * dy);

    }

    /***
     * 点到线的最短距离(矢量算法)
     * @param p 点[经度,纬度]
     * @param p1 线起点[经度,纬度]
     * @param p2 线终点[经度,纬度]
     * @return 距离(米)
     */
    public static Double point2line(double[] p, double[] p1, double[] p2) {

        //参数验证
        if (p == null || p1 == null || p2 == null) {
            return null;
        }
        if (p.length < 2 || p1.length < 2 || p2.length < 2) {
            return null;
        }

        //参数转换
        double x = p[0];
        double y = p[1];
        double x1 = p1[0];
        double y1 = p1[1];
        double x2 = p2[0];
        double y2 = p2[1];

        //取值p->p1
        double d1 = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
        if (d1 <= 0) {
            return point2point(new double[]{x, y}, new double[]{x1, y1});
        }

        //取值p->p2
        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (d1 >= d2) {
            return point2point(new double[]{x, y}, new double[]{x2, y2});
        }

        //取值p->垂直点
        double r = d1 / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        return point2point(new double[]{x, y}, new double[]{px, py});

    }

    /***
     * 点到线的最短距离
     * @param p 点[经度,纬度]
     * @param line 线[起点,...,终点]
     * @return 距离(米)
     */
    public static Double point2polyline(double[] p, double[][] line) {
        //参数验证
        if (p == null || line == null) {
            return null;
        }
        if (p.length < 2 || line.length < 2) {
            return null;
        }

        Double distance = null;
        for (int i = 0; i < line.length - 1; i++) {
            if (line[i].length < 2 || line[i + 1].length < 2) {
                continue;
            }

            Double d = point2line(p, line[i], line[i + 1]);
            if (d == null) {
                //N/A
            } else if (distance == null) {
                distance = d;
            } else if (d < distance) {
                distance = d;
            }
        }

        return distance;
    }

    /***
     * 线上最近点下标索引(矢量分析)
     * @param p 点[经度,纬度]
     * @param line 线[起点,...,终点]
     * @return 下标索引
     */
    public static Integer closest2polyline(double[] p, double[][] line) {

        //参数验证
        if (p == null || line == null) {
            return null;
        }
        if (p.length < 2 || line.length < 1) {
            return null;
        }

        Integer index = null;
        Double distance = null;
        for (int i = 0; i < line.length; i++) {
            if (line[i].length < 2) {
                continue;
            }

            Double d = point2point(p, line[i]);
            if (d == null) {
                //N/A
            } else if (distance == null) {
                distance = d;
                index = i;
            } else if (d < distance) {
                distance = d;
                index = i;
            }
        }

        return index;

    }

}
