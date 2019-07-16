package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
        private static int LAST_ARTICLE_ID = 0;

        private final List<Article> articles = new ArrayList<>();

        public List<Article> findAll() {
                return this.articles;
        }

        public void write(final Article article) {
                article.defaultSetting(++LAST_ARTICLE_ID);
                this.articles.add(article);
        }

        public Article find(final int articleId) {
                final int index = findIndexOfArticleById(articleId);
                return this.articles.get(index);
        }

        private int findIndexOfArticleById(final int articleId) {
                int index = Math.min(articleId, articles.size()) - 1;
                for (; index >= 0; index--) {
                        if (this.articles.get(index).getId() == articleId) {
                                return index;
                        }
                }
                throw new IllegalArgumentException("none LAST_ARTICLE_ID server error");
        }

        public void edit(final Article edited, final int articleId) {
                find(articleId).updateTo(edited);
        }

        public void delete(final int articleId) {
                final int index = findIndexOfArticleById(articleId);
                articles.remove(index);
        }

        public void deleteAll() {
                articles.clear();
        }

        public int getLastArticleId() {
                return LAST_ARTICLE_ID;
        }
}