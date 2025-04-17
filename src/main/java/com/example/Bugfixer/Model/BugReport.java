package com.example.Bugfixer.Model;

public class BugReport {

	 private String stacktrace;
	    private String codeSnippet;

	    // Getters and Setters
	    public String getStacktrace() {
	        return stacktrace;
	    }

	    public void setStacktrace(String stacktrace) {
	        this.stacktrace = stacktrace;
	    }

	    public String getCodeSnippet() {
	        return codeSnippet;
	    }

	    public void setCodeSnippet(String codeSnippet) {
	        this.codeSnippet = codeSnippet;
	    }
}
