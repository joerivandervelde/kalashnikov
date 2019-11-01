require(ggplot2)
require(scales)

setwd("~/github/kalashnikov/analysis/NrOfGamesVsNrOfPlayers")
stats <- read.table("data.tsv", sep="\t", header=T)
stats$players <- as.factor(stats$players)
ybreaks = c(1,10,100,1000,10000,100000)

ggplot() +
  theme_bw() + theme(panel.grid = element_blank()) +
  geom_boxplot(notch = TRUE, fill="lightgray", data = stats, aes(x = players, y = games), outlier.size = 0.5) +
  scale_y_log10(breaks = ybreaks, labels = comma) +
  ylab("Number of games played until there is a winner") +
  xlab("Number of players")
ggsave("NrOfGamesVsNrOfPlayers.png", width = 4, height = 4, dpi = 150)
