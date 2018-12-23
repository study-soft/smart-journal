import { afterMethod } from 'kaop-ts';

export const log = afterMethod(meta => {
    const methodName = `${meta.target.constructor.name}::${meta.key}`;
    console.log(`log-decorator: ${methodName} invoked with arguments -> `, meta.args);
    if (meta.exception) {
        console.log(`log-decorator: ${methodName} exception -> `, meta.exception);
    } else {
        console.log(`log-decorator: ${methodName} result -> `, meta.result);
    }
});
