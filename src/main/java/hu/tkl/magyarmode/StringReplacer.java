package hu.tkl.magyarmode;

/*
 * This file is a part of MagyarMode
 * Author: tkl, 20-07-2025 19:02
 * MagyarMode is licensed under the MIT License.
 *
 * Copyright (c) 2025 tkl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringReplacer {
  private final List<RegexReplacer> replacers = new ArrayList<>();

  public StringReplacer register(String regex, String replacement) {
    replacers.add(new RegexReplacer(regex, replacement));
    return this;
  }

  public String apply(String subject) {
    for(RegexReplacer replacer : replacers) {
      subject = replacer.apply(subject);
    }

    return subject;
  }

  private static final class RegexReplacer {
    private final Pattern pattern;
    private final String replacement;

    public RegexReplacer(String regex, String replacement) {
      this.pattern = Pattern.compile(regex);
      this.replacement = replacement;
    }

    public String apply(String subject) {
      return pattern.matcher(subject).replaceAll(replacement);
    }
  }
}
