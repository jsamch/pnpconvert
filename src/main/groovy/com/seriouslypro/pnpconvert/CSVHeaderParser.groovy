package com.seriouslypro.pnpconvert

interface CSVHeaderParser<TColumn extends Enum> {
    Map<TColumn, CSVHeader> parseHeaders(CSVInputContext context, String[] headerValues)
    TColumn parseHeader(CSVInputContext context, String headerValue)
}

class CSVHeaderParserBase<TColumn extends Enum> implements CSVHeaderParser<TColumn> {

    @Override
    Map<TColumn, CSVHeader> parseHeaders(CSVInputContext context, String[] headerValues) {
        int index = 0
        Map<TColumn, CSVHeader> headerMappings = headerValues.findResults { String headerValue ->

            index++
            try {
                TColumn column = parseHeader(context, headerValue)
                CSVHeader csvHeader = new CSVHeader(index: index - 1)
                new MapEntry(column, csvHeader)
            } catch (IllegalArgumentException e) {
                // ignore unknown headers
                return null
            }
        }.collectEntries()

        return headerMappings
    }

    @Override
    TColumn parseHeader(CSVInputContext context, String headerValue) {
        String candidate = headerValue.toUpperCase().replaceAll('[^A-Za-z0-9]', "_")
        return TColumn.valueOf(TColumn, candidate)
    }
}