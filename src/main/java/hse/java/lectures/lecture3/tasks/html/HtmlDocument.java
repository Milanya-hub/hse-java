package hse.java.lectures.lecture3.tasks.html;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

public class HtmlDocument {
    public HtmlDocument(String filePath) {
        this(Path.of(filePath));
    }

    public HtmlDocument(Path filePath) {
        String content = readFile(filePath);
        validate(content);
    }

    private String readFile(Path filePath) {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    private void validate(String content) {
        Deque<String> stack = new ArrayDeque<>();
        Set<String> tags = Set.of("html", "head", "body", "div", "p");
        boolean opened = false;
        boolean closed = false;
        boolean head = false;
        boolean body = false;

        int i = 0;
        while (i < content.length()) {
            if (content.charAt(i) == '<') {
                int j = content.indexOf('>', i);
                if (j == -1) {
                    throw new RuntimeException();
                }

                String tag = content.substring(i + 1, j).trim();

                boolean closing_tag = tag.startsWith("/");
                String tag_name;
                if (closing_tag) {
                    tag_name = tag.substring(1).trim().toLowerCase();
                } else {
                    String[] parts = tag.split("\\s+");
                    tag_name = parts[0].toLowerCase();
                }

                if (!tags.contains(tag_name)) {
                    throw new UnsupportedTagException("Unsupported tag: " + tag_name);
                }

                if (closing_tag) {
                    if (stack.isEmpty()) {
                        throw new UnexpectedClosingTagException("Unexpected closing tag: " + tag_name);
                    }

                    String expected = stack.pop();
                    if (!expected.equals(tag_name)) {
                        throw new MismatchedClosingTagException("Mismatched closing tag");
                    }

                    if (tag_name.equals("html")) {
                        closed = true;
                    }

                } else {
                    if (closed) {
                        throw new InvalidStructureException("Invalid structure");
                    }

                    if (tag_name.equals("html")) {
                        if (opened) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                        if (!stack.isEmpty()) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                        opened = true;
                    } else {
                        if (!opened) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                    }
                    if (tag_name.equals("head") || tag_name.equals("body")) {
                        if (stack.isEmpty() || !stack.peek().equals("html")) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                    }

                    if (tag_name.equals("head")) {
                        if (head) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                        if (body) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                        head = true;
                    }

                    if (tag_name.equals("body")) {
                        if (body) {
                            throw new InvalidStructureException("Invalid structure");
                        }
                        body = true;
                    }
                    stack.push(tag_name);
                }
                i = j + 1;
            } else {
                i++;
            }
        }

        if (!stack.isEmpty()) {
            throw new UnclosedTagException("Unclosed tags");
        }

        if (!opened || !closed) {
            throw new InvalidStructureException("Invalid structure");
        }
    }
}