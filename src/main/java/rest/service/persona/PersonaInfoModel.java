package rest.service.persona;

import help.utils.excel.bind.annotation.ExcelTableElement;

/**
 * @Author lgu2
 * @Create 2021/1/27
 */

@ExcelTableElement(sheetName = "Demo1",headerRowIndex = 1)
public class PersonaInfoModel {
    private String 编号;
    private String 姓名;
    private String 性别;
    private int 年龄;
    private String 地址;
    private String 电话;
    private String 邮件;
    private String 邮编;

    public String get编号() {
        return 编号;
    }

    public void set编号(String 编号) {
        this.编号 = 编号;
    }

    public String get姓名() {
        return 姓名;
    }

    public void set姓名(String 姓名) {
        this.姓名 = 姓名;
    }

    public String get性别() {
        return 性别;
    }

    public void set性别(String 性别) {
        this.性别 = 性别;
    }

    public int get年龄() {
        return 年龄;
    }

    public void set年龄(int 年龄) {
        this.年龄 = 年龄;
    }

    public String get地址() {
        return 地址;
    }

    public void set地址(String 地址) {
        this.地址 = 地址;
    }

    public String get电话() {
        return 电话;
    }

    public void set电话(String 电话) {
        this.电话 = 电话;
    }

    public String get邮件() {
        return 邮件;
    }

    public void set邮件(String 邮件) {
        this.邮件 = 邮件;
    }

    public String get邮编() {
        return 邮编;
    }

    public void set邮编(String 邮编) {
        this.邮编 = 邮编;
    }

    @Override
    public String toString() {
        return "人物信息：{" +
                "编号='" + 编号 + '\'' +
                ", 姓名='" + 姓名 + '\'' +
                ", 性别='" + 性别 + '\'' +
                ", 年龄=" + 年龄 +
                ", 地址='" + 地址 + '\'' +
                ", 电话='" + 电话 + '\'' +
                ", 邮件='" + 邮件 + '\'' +
                ", 邮编='" + 邮编 + '\'' +
                '}';
    }
}
