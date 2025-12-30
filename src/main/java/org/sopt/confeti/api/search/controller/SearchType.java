package org.sopt.confeti.api.search.controller;

import java.util.Objects;
import org.sopt.confeti.api.search.facade.SearchFacade;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

public enum SearchType {
    AID {
        @Override
        public SearchResultDTO search(SearchFacade facade, String aid, Long pid, String term) {
            return facade.getHomeSearchResultWithAid(aid);
        }
    },
    PID {
        @Override
        public SearchResultDTO search(SearchFacade facade, String aid, Long pid, String term) {
            return facade.getHomeSearchResultWithPid(pid);
        }
    },
    TERM {
        @Override
        public SearchResultDTO search(SearchFacade facade, String aid, Long pid, String term) {
            return facade.getHomeSearchResultWithTerm(term);
        }
    };

    public static SearchType resolve(String aid, Long pid, String term) {
        if (Objects.nonNull(aid)) {
            return AID;
        }
        if (Objects.nonNull(pid)) {
            return PID;
        }
        if (Objects.nonNull(term)) {
            return TERM;
        }

        throw new ConfetiException(ErrorMessage.BAD_REQUEST);
    }

    public abstract SearchResultDTO search(SearchFacade searchFacade, String aid, Long pid,
        String term);
}
