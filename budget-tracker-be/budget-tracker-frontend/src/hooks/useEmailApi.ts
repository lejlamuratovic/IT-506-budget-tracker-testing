import { useMutation } from "@tanstack/react-query";

import { EmailRequest } from "../types";
import { sendUserReportEmail } from "../api";

export const useSendUserReportEmail = () => {
    return useMutation<string, Error, EmailRequest>({
        mutationFn: sendUserReportEmail,
    });
};
