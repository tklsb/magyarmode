package hu.tkl.magyarmode;

/*
 * This file is a part of MagyarMode
 * Author: tkl, 20-07-2025 18:15
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

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MagyarMode implements ModInitializer {
  private static final StringReplacer REPLACER = new StringReplacer()
      .register("(^|\\s)e([zZ])($|\\s)", "$1ë$2$3")
      .register("(^|\\s)E([zZ])($|\\s)", "$1Ë$2$3");

  private static final Path CONFIG_FILE = Path.of("config/magyarmode");

  private static boolean ENABLED = true;

  private static void writeConfig() {
    try {
      Files.write(
          CONFIG_FILE,
          new byte[] {
              (byte)(ENABLED ? '1' : '0')
          });
    } catch(IOException ignored) {
      // Who cares
    }
  }

  @Override
  public void onInitialize() {
    // Load ENABLED state, if CONFIG_FILE exists
    try {
      byte[] config = Files.readAllBytes(CONFIG_FILE);

      if(config.length > 0 && config[0] != '1') {
        ENABLED = false;
      }
    } catch(IOException ignored) {}

    // Remember ENABLED state on shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(MagyarMode::writeConfig));

    // Register command "/magyarmode" to toggle ENABLED state
    ClientCommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess) -> dispatcher.register(
            ClientCommandManager.literal("magyarmode")
                                .executes(commandContext -> {
                                  ENABLED = !ENABLED;

                                  writeConfig();

                                  commandContext.getSource()
                                                .sendFeedback(Text.literal(
                                                    (ENABLED ? "En" : "Dis")
                                                        + "abled " +
                                                        "MagyarMode"));

                                  return 0;
                                })));
  }

  public static String fixMessage(String chatMessage) {
    return ENABLED ? REPLACER.apply(chatMessage) : chatMessage;
  }
}