library(ggplot2)
setwd("~/github/kalashnikov/analysis/WinBiasVsNrOfPlayers/")
stats <- read.table("data.tsv", sep="\t", header=T)
stats$players <- as.factor(stats$players)

ggplot() +
  theme_bw() + theme(panel.grid = element_blank()) +
  geom_boxplot(notch = TRUE, fill="lightgray", data = stats, aes(x = players, y = percBetterWinningOdds), outlier.shape = NA) +
  geom_jitter(width=0.2, data = stats, aes(x = players, y = percBetterWinningOdds), size=0.2) +
  xlab("Number of players") +
  ylab("Increased % chance of starting player winning") +
  scale_y_continuous(breaks=seq(-10,50,5))
ggsave("WinBiasVsNrOfPlayers.png", width = 4, height = 4, dpi=150)
