library(ggplot2)
setwd("~/github/kalashnikov/analysis/BetterGoldenGun//")
stats <- read.table("data.tsv", sep="\t", header=T)
stats$goldenStrat <- as.factor(stats$goldenStrat)
stats$goldenGunDmg <- as.factor(stats$goldenGunDmg)

ggplot() +
  theme_bw() + theme(panel.grid = element_blank(), legend.position = c(0.75, 0.2)) +
  theme(legend.position="bottom") +
  geom_jitter(width=0.3,data = stats, aes(x = goldenGunDmg, y = wins, col=goldenStrat), size=0.2) +
  scale_x_discrete(breaks=seq(1,20,1)) +
  guides(colour = guide_legend(nrow = 2, override.aes = list(size=2))) +
  labs(col = "Likelihood to wait \nfor golden gun") +
  xlab("Damage done by the golden gun") +
  ylab("Percentage of game series won") +
  scale_y_continuous(breaks=c(0,100,200,300,400,500), labels=c("0%","10%","20%","30%","40%","50%"))
ggsave("BetterGoldenGun.png", width = 4, height = 4, dpi=150)
