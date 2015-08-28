package org.example;

import org.avaje.agentloader.AgentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load both the entity bean agent and the type query agent.
 */
public class BaseTestCase {

  protected static Logger logger = LoggerFactory.getLogger(BaseTestCase.class);

  static {
    logger.debug("... preStart");
    if (!AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-typequery-agent", "debug=1")) {
      logger.info("avaje-ebeanorm-typequery-agent not found in classpath - not dynamically loaded");
    }
    if (!AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1;packages=org.example.domain")) {
      logger.info("avaje-ebeanorm-agent not found in classpath - not dynamically loaded");
    }

  }
}
