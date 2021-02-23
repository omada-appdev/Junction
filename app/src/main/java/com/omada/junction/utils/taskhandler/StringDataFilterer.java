package com.omada.junction.utils.taskhandler;

public class StringDataFilterer implements DataFilterer<String> {

    private String data;

    public enum CaseType {
        CASE_TYPE_TITLE,
        CASE_TYPE_SMALL,
        CASE_TYPE_CAPITAL
    }

    private StringDataFilterer(String data) {
        this.data = data;
    }

    public static StringDataFilterer of(String data) {
        return new StringDataFilterer(data);
    }

    public StringDataFilterer escapeHtmlTags(String data) {
        this.data = data
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        return this;
    }

    public StringDataFilterer unescapeHtmlTags(String data) {
        this.data = data
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", ">")
                .replace("&amp;", "&");
        return this;
    }

    public StringDataFilterer filterString(String string, String repl) {
        this.data = data.replace(string, repl);
        return this;
    }

    public StringDataFilterer stripSpaces(String data) {
        this.data = data.trim();
        return this;
    }

    public StringDataFilterer changeCase(CaseType caseType) {
        switch (caseType) {
            case CASE_TYPE_SMALL:
                this.data = data.toLowerCase();
                break;
            case CASE_TYPE_TITLE:
                break;
            case CASE_TYPE_CAPITAL:
                this.data = data.toUpperCase();
                break;
        }
        return this;
    }

    @Override
    public String get() {
        return data;
    }
}
