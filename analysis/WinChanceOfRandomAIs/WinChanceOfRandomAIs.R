library(ggplot2)
setwd("~/github/kalashnikov/analysis/WinChanceOfRandomAIs")
stats <- read.table("data.tsv", sep="\t", header=T)

ggplot() +
  theme_bw() + theme(panel.grid = element_blank()) +
  geom_point(data = stats, aes(x = wins, y = waitForGoldenGun), size=0.5) +
  scale_x_continuous(limits = c(0,700), breaks=c(0,100,200,300,400,500,600,700), labels=c("0%","10%","20%","30%","40%","50%","60%","70%")) +
  xlab("Percentage of game series won") +
  ylab("Likelihood to wait for the golden gun")
ggsave("WaitForGoldenGun.png", width = 4, height = 4, dpi=150)

ggplot() +
  theme_bw() + theme(panel.grid = element_blank()) +
  geom_point(data = stats, aes(x = wins, y = chooseScrapOverUnknownShelf), size=0.5) +
  scale_x_continuous(limits = c(0,700), breaks=c(0,100,200,300,400,500,600,700), labels=c("0%","10%","20%","30%","40%","50%","60%","70%")) +
  xlab("Percentage of game series won") +
  ylab("Likelihood to choose scrap over unknown shelf card")
ggsave("ScrapOverShelf.png", width = 4, height = 4, dpi=150)

ggplot() +
  theme_bw() + theme(panel.grid = element_blank()) +
  geom_point(data = stats, aes(x = wins, y = discardDuplicatesBeforeUselessCards), size=0.5) +
  scale_x_continuous(limits = c(0,700), breaks=c(0,100,200,300,400,500,600,700), labels=c("0%","10%","20%","30%","40%","50%","60%","70%")) +
  xlab("Percentage of game series won") +
  ylab("Likelihood to discard duplicates before useless cards")
ggsave("DuplicatesBeforeUseless.png", width = 4, height = 4, dpi=150)
